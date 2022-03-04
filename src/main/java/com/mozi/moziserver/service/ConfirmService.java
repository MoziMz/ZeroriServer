package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.DeclarationType;
import com.mozi.moziserver.model.req.*;
import com.mozi.moziserver.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmService {
    private final UserRepository userRepository;

    private final ChallengeRepository challengeRepository;

    private final ConfirmRepository confirmRepository;

    private final DeclarationRepository declarationRepository;

    private final ConfirmStickerRepository confirmStickerRepository;

    private final UserStickerRepository userStickerRepository;

    private final StickerImgRepository stickerImgRepository;

    //인증 생성
    @Transactional
    public void createConfirm(Long userSeq, Long seq, ReqConfirmCreate reqConfirmCreate){

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Challenge challenge=challengeRepository.findById(seq)
                .orElseThrow(ResponseError.BadRequest.INVALID_SEQ::getResponseException);

        //신고안됨
        Byte state=0;

        Confirm confirm=Confirm.builder()
                .user(user)
                .challenge(challenge)
                .date(reqConfirmCreate.getDate())
                .imgUrl(reqConfirmCreate.getImgUrl())
                .confirmState(state)
                .build();

        try {
            confirmRepository.save(confirm);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }

    }

    @Transactional
    public List<Confirm> getAll(ReqConfirmList req){
        return confirmRepository.findAllList(req.getPrevLastConfirmSeq(),req.getPageSize());
    }

    // 챌린지별 인증 조회
    @Transactional
    public List<Confirm> getConfirmList(Long seq,ReqConfirmList req) {
        return confirmRepository.findByChallengeByOrderDesc(seq,req.getPrevLastConfirmSeq(),req.getPageSize());
    }

    @Transactional
    public List<Confirm> getUserConfirmList(Long userSeq,ReqConfirmList req){
        return confirmRepository.findByUserByOrderDesc(userSeq,req.getPrevLastConfirmSeq(),req.getPageSize());
    }

    //인증 하나 조회
    @Transactional
    public Confirm getConfirm(Long confirmSeq) {
        return confirmRepository.findBySeq(confirmSeq);
    }

    //confirmSticker 조회
    @Transactional
    public List<ConfirmSticker> getConfirmStickerList(Long seq){
        return confirmStickerRepository.findAllBySeq(seq);
    }

    @Transactional
    public void deleteConfirm(Long userSeq, Long confirmSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Confirm confirm=getConfirm(confirmSeq);

        if(!confirm.getUser().equals(user)) throw ResponseError.BadRequest.INVALID_USER.getResponseException();

        try {
            int deleteCount = confirmRepository.deleteConfirm(confirmSeq);
            if (deleteCount == 0) {
                // 동시성 처리: 지울려고 했는데 못 지웠으면 함수실행을 끝낸다.
                throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException();
            }
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException(); // for duplicate exception
        } // FIXME DuplicateKeyException
    }

    @Transactional
    public void updateConfirmState(Confirm confirm){

        Byte state=1;

        confirmRepository.updateDeclarationState(
                confirm,state
        );
    }

    //신고 생성
    @Transactional
    public void createDeclaration(Long confirmSeq,DeclarationType type){

        Confirm confirm=confirmRepository.findBySeq(confirmSeq);


        updateConfirmState(confirm);

        Declaration declaration=Declaration.builder()
                .confirm(confirm)
                .declarationType(type)
                .build();

        try {
            declarationRepository.save(declaration);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }

    }

    @Transactional
    public List<StickerImg> getStickerImgList(List<Long> stickerSeqList){
        return stickerImgRepository.findAllBySeq(stickerSeqList);
    }

    public List<UserStickerImg> getUserStickerImg(Long userSeq) {

            List<Long> userStickerSeqList=userStickerRepository.stickerSeqfindByUserSeq(userSeq);

            List<StickerImg> stickerImgList = stickerImgRepository.findAll();
            Set<Long> downloadedStickers = new HashSet();

            for ( Long userStickerSeq : userStickerSeqList) {
                downloadedStickers.add(userStickerSeq);
            }

            List<UserStickerImg> userStickerImgs=new ArrayList<UserStickerImg>();
            for ( StickerImg stickerImg : stickerImgList ) {
                if(downloadedStickers.contains(stickerImg.getSeq())) {
                    userStickerImgs.add(new UserStickerImg(stickerImg, true));
                }
                else
                    userStickerImgs.add(new UserStickerImg(stickerImg, false));

            }

            return userStickerImgs;

    }

    //UserSticker 생성
    @Transactional
    public void createUserSticker(Long userSeq, ReqUserStickerList userStickerList){

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        List<Long> stickerSeqList=userStickerList.getStickerSeqList();

        List<UserSticker> newUserStickerList=new ArrayList<UserSticker>();


        List<StickerImg> stickerImgList=getStickerImgList(stickerSeqList);
        for(StickerImg stickerImg:stickerImgList){
            UserStickerId userStickerId=new UserStickerId(user,stickerImg);
                 UserSticker userSticker=UserSticker.builder()
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

    //ConfirmSticker 생성
    @Transactional
    public void createConfirmSticker(Long userSeq,Long confirmSeq,ReqConfirmSticker reqConfirmSticker){

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Confirm confirm=confirmRepository.findBySeq(confirmSeq);

        //confirmSticker는 자기 자신에 스토리에 스티커를 붙이지 못한다.
        if(userSeq==confirm.getUser().getSeq()) throw ResponseError.BadRequest.INVALID_USER.getResponseException();

        //true면 존재한다.
        Boolean createdCheck=confirmStickerRepository.findByUserAndConfirmSeq(userSeq,confirmSeq);

        //하나만 붙일수있다. confirmSeq가 같고 userSeq가 같으면 못붙인다.
        if(createdCheck==true)  throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();

        Optional<StickerImg> stickerImg=stickerImgRepository.findById(reqConfirmSticker.getStickerSeq());

        UserStickerId userStickerId=new UserStickerId(user,stickerImg.get());

        UserSticker userSticker=userStickerRepository.findById(userStickerId)
                .orElseThrow(ResponseError.NotFound.USER_STICKER_NOT_EXISTS::getResponseException);

        ConfirmSticker confirmSticker=ConfirmSticker.builder()
                .confirm(confirm)
                .user(user)
                .stickerImg(stickerImg.get())
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

}
