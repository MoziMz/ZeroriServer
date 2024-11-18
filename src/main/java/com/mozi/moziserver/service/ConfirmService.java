package com.mozi.moziserver.service;

import com.mozi.moziserver.common.JpaUtil;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.ConfirmListType;
import com.mozi.moziserver.model.mappedenum.ConfirmReportType;
import com.mozi.moziserver.model.mappedenum.ConfirmStateType;
import com.mozi.moziserver.model.mappedenum.PointReasonType;
import com.mozi.moziserver.model.req.ReqConfirmOfUser;
import com.mozi.moziserver.model.req.ReqConfirmSticker;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.model.req.ReqUserStickerList;
import com.mozi.moziserver.model.res.ResWeekConfirm;
import com.mozi.moziserver.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmService {

    private final UserChallengeService userChallengeService;
    private final UserRewardService userRewardService;
    private final AsyncService asyncService;
    private final S3ImageService s3ImageService;

    private final ChallengeRepository challengeRepository;
    private final ConfirmRepository confirmRepository;
    private final ConfirmReportRepository confirmReportRepository;
    private final ConfirmStickerRepository confirmStickerRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final ConfirmLikeRepository confirmLikeRepository;
    private final StickerRepository stickerRepository;
    private final UserStickerRepository userStickerRepository;

    private final PlatformTransactionManager transactionManager;

    //인증 생성
    public void createConfirm(User user, Long challengeSeq, MultipartFile image) {

        Challenge challenge = challengeRepository.findById(challengeSeq)
                .orElseThrow(ResponseError.BadRequest.INVALID_SEQ::getResponseException);

        //신고안됨
        Byte state = 0;

        String imgUrl = null;
        try {
            imgUrl = s3ImageService.uploadFile(image, "confirm");
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        Confirm confirm = Confirm.builder()
                .user(user)
                .challenge(challenge)
                .imgUrl(imgUrl)
                .state(ConfirmStateType.ACTIVE)
                .build();

        withTransaction(() -> {
            UserChallenge curUserChallenge = userChallengeService.getActiveUserChallenge(user.getSeq(), challenge)
                    .orElseThrow(ResponseError.NotFound.USER_CHALLENGE_NOT_EXISTS::getResponseException);

            try {
                confirmRepository.save(confirm);
            } catch (Exception e) {
                throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
            }

            userChallengeService.updateUserChallengeResultComplete(curUserChallenge, LocalDate.now());

            ChallengeRecord challengeRecord = challengeRecordRepository.findByChallenge(challenge);

            challengeRecord.setTotalPlayerConfirmCnt(challengeRecord.getTotalPlayerConfirmCnt() + 1);
            challengeRecordRepository.save(challengeRecord);

            userRewardService.incrementPoint(user, PointReasonType.CHALLENGE_CONFIRM, challenge.getPoint());

        });

        asyncService.sendNewAnimalNotification(user);

        asyncService.sendAnimalMention(user);

        //인증 이미지 검증
        asyncService.verifyConfirm(confirm.getSeq(), imgUrl, challenge.getName());
    }

    public List<Confirm> getConfirmList(User user, ReqList req, ConfirmListType confirmListType) {

        List<Confirm> confirmList = new ArrayList<>();

        if (confirmListType.equals(ConfirmListType.RECENT)) {
            LocalDateTime now = LocalDateTime.now();

            LocalDateTime startDateTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth() - 6, 0, 0, 0);

            confirmList = confirmRepository.findByPeriodAndPaging(startDateTime, req.getPrevLastSeq(), req.getPageSize());

        } else if (confirmListType.equals(ConfirmListType.ALL)) {
            confirmList = confirmRepository.findAll(req.getPrevLastSeq(), req.getPageSize());
        }

        if (confirmList.isEmpty() == false) {

            toRandomList(confirmList);

            setConfirmLike(user, confirmList);

            setConfirmReported(user, confirmList);

        }

        // TODO 이 방식이면 페이징 제대로 동작안함
        return confirmList.stream()
                .filter(c -> !c.isReported())
                .collect(Collectors.toList());
    }

    // 챌린지별 인증 조회
    public List<Confirm> getConfirmListByChallenge(User user, Long challengeSeq, ReqList req) {
        final Challenge challenge = challengeRepository.findById(challengeSeq)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_NOT_EXISTS::getResponseException);

        List<Confirm> confirmList = confirmRepository.findAllByChallenge(challenge, req.getPrevLastSeq(), req.getPageSize());

        setConfirmLike(user, confirmList);

        setConfirmReported(user, confirmList);

        return confirmList.stream()
                .filter(c -> !c.isReported())
                .collect(Collectors.toList());
    }

    public List<Confirm> getConfirmListByUserChallenge(User user, Long userChallengeSeq) {
        final UserChallenge userChallenge = userChallengeService.getUserChallenge(userChallengeSeq);

        List<Confirm> confirmList = confirmRepository.findByUserAndPeriod(
                userChallenge.getUser(),
                userChallenge.getChallenge(),
                userChallenge.getStartDate().atTime(0, 0),
                userChallenge.getEndDate().plusDays(1).atTime(0, 0)
        );

        setConfirmLike(user, confirmList);

        setConfirmReported(user, confirmList);

        return confirmList.stream()
                .filter(c -> !c.isReported())
                .collect(Collectors.toList());
    }

    public Optional<Confirm> getConfirmByChallenge(Challenge challenge) {
        return confirmRepository.findByChallenge(challenge);
    }

    private void setConfirmLike(User user, List<Confirm> confirmList) {

        List<ConfirmLike> confirmLikeList = confirmLikeRepository.findAllByUserAndConfirmsIn(user, confirmList);
        HashSet<Long> likedConfirmSeqSet = new HashSet<>(confirmLikeList.stream()
                .map(confirmLike -> confirmLike.getConfirm().getSeq())
                .collect(Collectors.toSet()));

        for (Confirm confirm : confirmList) {
            boolean isLiked = likedConfirmSeqSet.contains(confirm.getSeq());
            confirm.setLiked(isLiked);
        }

        //return confirmList;
    }

    private void setConfirmReported(User user, List<Confirm> confirmList) {

        List<ConfirmReport> confirmReportList = confirmReportRepository.findByUser(user);
        HashSet<Long> reportedConfirmSeqSet = new HashSet<>(confirmReportList.stream()
                .map(declaration -> declaration.getConfirm().getSeq())
                .collect(Collectors.toSet()));

        for (Confirm confirm : confirmList) {
            boolean isReported = reportedConfirmSeqSet.contains(confirm.getSeq());
            confirm.setReported(isReported);
        }
    }

    @Transactional // TODO delete transaction where get method
    public List<Confirm> getUserConfirmList(User user, ReqList req) {

        List<Confirm> confirmList = confirmRepository.findByUserByOrderDesc(user, req.getPrevLastSeq(), req.getPageSize());

        setConfirmLike(user, confirmList);

        return confirmList;
    }

    //인증 하나 조회
    @Transactional
    public Confirm getConfirm(Long confirmSeq) {
        return confirmRepository.findBySeq(confirmSeq);
    }

    @Transactional
    public void deleteConfirm(User user, Long confirmSeq) {

        Confirm confirm = getConfirm(confirmSeq);

        if (!confirm.getUser().equals(user)) {
            throw ResponseError.BadRequest.INVALID_USER.getResponseException();
        }

        confirm.setState(ConfirmStateType.DELETED);
        confirmRepository.save(confirm);
    }

    @Transactional
    public void createReport(User user, Long confirmSeq, ConfirmReportType type) {

        Confirm confirm = confirmRepository.findBySeq(confirmSeq);
        if (confirm == null) {
            throw ResponseError.NotFound.CONFIRM_NOT_EXISTS.getResponseException();
        }

        if (user.equals(confirm.getUser())) {
            throw ResponseError.BadRequest.INVALID_DECLARATION.getResponseException();
        }

        ConfirmReport confirmReport = confirmReportRepository.findByConfirmAndUser(confirm, user);
        if (confirmReport != null) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();
        }

        confirmRepository.updateStateSupportedCnt(confirm, ConfirmStateType.REPORTED, confirm.getReportedCnt() + 1);

        confirmReport = ConfirmReport.builder()
                .confirm(confirm)
                .user(user)
                .confirmReportType(type)
                .build();

        try {
            confirmReportRepository.save(confirmReport);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }

    }

    public void createConfirmLike(User user, Long confirmSeq) {

        Confirm confirm = confirmRepository.findBySeq(confirmSeq);
        if (confirm == null) {
            throw ResponseError.NotFound.CONFIRM_NOT_EXISTS.getResponseException();
        }

        ConfirmLike confirmLike = ConfirmLike.builder()
                .confirm(confirm)
                .user(user)
                .build();

        try {
            confirmLikeRepository.save(confirmLike);
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_CONFIRM_LIKE.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        confirmRepository.incrementLikeCnt(confirm.getSeq());
    }

    @Transactional
    public void deleteConfirmLike(User user, Long confirmSeq) {

        Confirm confirm = confirmRepository.findBySeq(confirmSeq);
        if (confirm == null) {
            throw ResponseError.NotFound.CONFIRM_NOT_EXISTS.getResponseException();
        }

        try {
            int deleteCnt = confirmLikeRepository.deleteByConfirmSeqAndUserSeq(confirm.getSeq(), user.getSeq());
            if (deleteCnt != 1) {
                throw ResponseError.NotFound.CONFIRM_LIKE_NOT_EXISTS.getResponseException();
            }
        } catch (Exception e) {
            throw ResponseError.NotFound.CONFIRM_LIKE_NOT_EXISTS.getResponseException();
        }

        confirmRepository.decrementLikeCnt(confirm.getSeq());
    }

    public ResWeekConfirm getWeekConfirm(ConfirmListType confirmListType) {

        LocalDateTime now = LocalDateTime.now();

        List<Confirm> confirmList = new ArrayList<>();

        if (confirmListType.equals(ConfirmListType.RECENT)) {
            LocalDateTime startDateTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth() - 6, 0, 0, 0);
            confirmList = confirmRepository.findByCreatedAt(startDateTime);

        } else if (confirmListType.equals(ConfirmListType.ALL)) {
            confirmList = confirmRepository.findAll();
        }

        List<User> userList = confirmList.stream().map(Confirm::getUser).collect(Collectors.toList());

        userList = userList.stream().distinct().collect(Collectors.toList());

        return ResWeekConfirm.of(userList, confirmList);
    }

    public List<Confirm> getConfirmListAboutPeriod(User user, ReqConfirmOfUser req) {

        Challenge challenge = challengeRepository.findById(req.getChallengeSeq())
                .orElseThrow(ResponseError.BadRequest.INVALID_SEQ::getResponseException);

        List<Confirm> confirmList = confirmRepository.findByUserAndPeriod(
                user, challenge, req.getStartDate().atTime(0, 0), req.getEndDate().plusDays(1).atTime(0, 0));

        setConfirmLike(user, confirmList);

        return confirmList;
    }

    public void toRandomList(List<Confirm> confirmList) {

        Confirm lastConfirm = confirmList.remove(confirmList.size() - 1);

        Collections.shuffle(confirmList);

        confirmList.add(lastConfirm);
    }

    // TODO DELETE OR ERASE ME
    @Transactional
    public List<ConfirmSticker> getConfirmStickerList(Long seq) {
        return confirmStickerRepository.findAllBySeq(seq);
    }

    @Transactional
    public List<Sticker> getStickerList(List<Long> stickerSeqList) {
        return stickerRepository.findAllBySeq(stickerSeqList);
    }


    @Transactional
    public List<Sticker> getSticker() {
        List<Sticker> stickerList = stickerRepository.findAll();

        return stickerList;
    }

    // UserSticker 생성
    @Transactional
    public void createUserSticker(User user, ReqUserStickerList userStickerList) {

        List<Long> stickerSeqList = userStickerList.getStickerSeqList();

        List<UserSticker> newUserStickerList = new ArrayList<UserSticker>();


        List<Sticker> stickerList = getStickerList(stickerSeqList);
        for (Sticker sticker : stickerList) {
            UserStickerId userStickerId = new UserStickerId(user, sticker);
            UserSticker userSticker = UserSticker.builder()
                    .id(userStickerId)
                    .build();
            newUserStickerList.add(userSticker);


        }
        try {
            userStickerRepository.saveAll(newUserStickerList);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }

    }

    // ConfirmSticker 생성
    @Transactional
    public void createConfirmSticker(User user, Long confirmSeq, ReqConfirmSticker reqConfirmSticker) {

        Confirm confirm = confirmRepository.findBySeq(confirmSeq);

        //confirmSticker는 자기 자신에 스토리에 스티커를 붙이지 못한다.
        if (user == confirm.getUser()) throw ResponseError.BadRequest.INVALID_USER.getResponseException();

        //true면 존재한다.
        Boolean createdCheck = confirmStickerRepository.findByUserAndConfirmSeq(user.getSeq(), confirmSeq);

        //하나만 붙일수있다. confirmSeq가 같고 userSeq가 같으면 못붙인다.
        if (createdCheck == true) throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();

        Optional<Sticker> sticker = stickerRepository.findById(reqConfirmSticker.getStickerSeq());

        UserStickerId userStickerId = new UserStickerId(user, sticker.get());

        UserSticker userSticker = userStickerRepository.findById(userStickerId)
                .orElseThrow(ResponseError.NotFound.USER_STICKER_NOT_EXISTS::getResponseException);

        ConfirmSticker confirmSticker = ConfirmSticker.builder()
                .confirm(confirm)
                .user(user)
                .sticker(sticker.get())
                .locationX(reqConfirmSticker.getLocationX())
                .locationY(reqConfirmSticker.getLocationY())
                .angle(reqConfirmSticker.getAngle())
                .inch(reqConfirmSticker.getInch())
                .build();

        try {
            confirmStickerRepository.save(confirmSticker);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }
    }

    private void withTransaction(Runnable runnable) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();

        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            runnable.run();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
    }
}