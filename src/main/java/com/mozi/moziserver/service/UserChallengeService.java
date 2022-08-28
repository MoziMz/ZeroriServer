package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.UserChallengeResultType;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import com.mozi.moziserver.model.req.ReqChallengeAndDate;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.model.req.ReqUserChallengeCreate;
import com.mozi.moziserver.model.req.ReqUserChallengeList;
import com.mozi.moziserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserChallengeService {
    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeStatisticsRepository challengeStatisticsRepository;
    private final ChallengeStatisticsUserUniqCheckRepository challengeStatisticsUserUniqCheckRepository;
    private final UserRewardRepository userRewardRepository;
    private final UserChallengeRecordRepository userChallengeRecordRepository;

    public UserChallenge getUserChallenge(Long userChallengeSeq) {
        return userChallengeRepository.findBySeq(userChallengeSeq)
                .orElseThrow(ResponseError.NotFound.USER_CHALLENGE_NOT_EXISTS::getResponseException);
    }

    private UserChallenge getUserChallenge(Long userSeq, Long userChallengeSeq) {
        UserChallenge userChallenge = userChallengeRepository.findById(userChallengeSeq)
                .orElseThrow(ResponseError.NotFound.USER_CHALLENGE_NOT_EXISTS::getResponseException);

        if (!userChallenge.getUser().getSeq().equals(userSeq)) {
            throw ResponseError.Forbidden.NO_AUTHORITY.getResponseException();
        }

        return userChallenge;
    }

    public Optional<UserChallenge> getActiveUserChallenge(Long userSeq, Challenge challenge) {

        return userChallengeRepository.findUserChallengeByUserSeqAndChallengeAndStates(userSeq, challenge, UserChallengeStateType.activeTypes);
    }

    /**
     * 유저챌린지 리스트 조회
     *
     * @param userSeq
     * @param req
     * @return
     */
    public List<UserChallenge> getUserChallengeList(Long userSeq, ReqList req) {
        return userChallengeRepository.findAllByUserSeq(
                userSeq,
                req.getPageSize(),
                req.getPrevLastSeq()
        );
    }

    public List<UserChallenge> getUserChallengeList(Long userSeq, ReqUserChallengeList req) {
        return userChallengeRepository.findAllByPeriod(
                userSeq,
                req.getStartDate(),
                req.getEndDate(),
                req.getChallengeSeq()
        );
    }

    public List<UserChallenge> getEndUserChallengeList(Long userSeq) {
        return userChallengeRepository.findAllByStateAndCheckedState(
                userSeq
        );
    }

    public boolean isCreatableUserChallenge(User user, Challenge challenge, LocalDate startDate) {
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today)) {
            throw ResponseError.BadRequest.PAST_START_DATE.getResponseException();
        }

        boolean isExists = userChallengeRepository.findUserChallengeByUserSeqAndChallengeAndStates(user.getSeq(), challenge, Arrays.asList(UserChallengeStateType.PLAN, UserChallengeStateType.DOING))
                .isPresent();
        if (isExists) {
            throw ResponseError.BadRequest.ALREADY_EXISTS_USER_CHALLENGE_IN_PROGRESS.getResponseException();
        }

        Optional<UserChallenge> stoppedUserChallenge = userChallengeRepository.findUserChallengeByUserSeqAndChallengeAndStates(user.getSeq(), challenge, Arrays.asList(UserChallengeStateType.STOP));

        if (stoppedUserChallenge.isPresent()) {
            LocalDate stoppedUserChallengeStartDate = stoppedUserChallenge.get().getStartDate();
            LocalDate stoppedUserChallengeEndDate = stoppedUserChallenge.get().getEndDate();
            if (stoppedUserChallengeStartDate.equals(startDate) && (stoppedUserChallengeStartDate.isBefore(stoppedUserChallengeEndDate) || stoppedUserChallengeStartDate.equals(stoppedUserChallengeEndDate))) {
                throw ResponseError.BadRequest.TODAY_STOPPED_CHALLENGE.getResponseException();
            }
        }

        return true;
    }

    public void checkCreatableUserChallenge(Long userSeq, ReqChallengeAndDate req) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Challenge challenge = challengeRepository.findById(req.getChallengeSeq())
                .orElseThrow(ResponseError.NotFound.CHALLENGE_NOT_EXISTS::getResponseException);

        isCreatableUserChallenge(user, challenge, req.getStartDate());
    }

    @Transactional
    public void createUserChallenge(Long userSeq, ReqUserChallengeCreate req) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Challenge challenge = challengeRepository.findById(req.getChallengeSeq())
                .orElseThrow(ResponseError.NotFound.CHALLENGE_NOT_EXISTS::getResponseException);

        isCreatableUserChallenge(user, challenge, req.getStartDate());

        LocalDate today = LocalDate.now();

        UserChallengeStateType stateType = UserChallengeStateType.PLAN;
        if (req.getStartDate().isEqual(today)) {
            stateType = UserChallengeStateType.DOING;
        }

        final UserChallenge userChallenge = UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .startDate(req.getStartDate())
                .endDate(req.getStartDate().plusDays(6))
                .state(stateType)
                .build();

        userChallengeRepository.save(userChallenge);

        Optional<UserChallengeRecord> optionalUserChallengeRecord = userChallengeRecordRepository.findByChallengeAndUser(challenge.getSeq(), userSeq);

        if (!optionalUserChallengeRecord.isPresent()) {
            final UserChallengeRecord userChallengeRecord = UserChallengeRecord.builder()
                    .challenge(challenge)
                    .user(user)
                    .build();

            userChallengeRecordRepository.save(userChallengeRecord);
        }

        // TODO 첫번째 요청인지 확인하고 아니면 에러를 던진다.
    }

    public void updateUserChallengeResultComplete(UserChallenge userChallenge, LocalDate date) {
        LocalDate startDate = userChallenge.getStartDate();
        boolean isDateOutOfRange = startDate.isAfter(date) || date.isAfter(startDate.plusDays(6));
        if (isDateOutOfRange) {
            throw ResponseError.BadRequest.INVALID_DATE.getResponseException();
        }

        // TODO Duration 으로 변경
//        Duration duration = Duration.between(startDate, date);
//        Integer turn = (int)duration.toDays();
        Period period = Period.between(startDate, date);
        Integer turn = period.getDays();

        // TODO 원본값을 넣어놓고 where 조건으로 넣는다. (UPDATE WHERE 동시성 프로그래밍)

        // TODO COMPLETE 체크
        userChallenge.getResultList().get(turn).setResult(UserChallengeResultType.COMPLETE);

        int confirmCnt = (int) userChallenge.getResultList()
                .stream()
                .filter(c -> c.getResult().equals(UserChallengeResultType.COMPLETE))
                .count();

        userChallenge.setTotalConfirmCnt(confirmCnt);

        int acquisitionPoints = userChallenge.getChallenge().getPoint() * confirmCnt;

        userChallenge.setAcquisitionPoints(acquisitionPoints);

        UserReward userReward = userRewardRepository.findByUser(userChallenge.getUser());

        userReward.setPoint(userReward.getPoint() + userChallenge.getChallenge().getPoint());

        userRewardRepository.save(userReward);

        userChallengeRepository.save(userChallenge);

        updateUserChallengeRecord(userChallenge.getChallenge(), userChallenge.getUser(), 1, acquisitionPoints);

        updateChallengeStatisticsByUserChallenge(userChallenge, date);
    }

    private void updateChallengeStatisticsByUserChallenge(UserChallenge userChallenge, LocalDate date) {
        // TODO 인증이 취소되었을 -1 되는 부분은 추후에 구현하기 (현재 인증을 했을때 +1 되는 부분에 대한 처리만 구현함)

        LocalDate startDate = userChallenge.getStartDate();
        boolean isDateOutOfRange = startDate.isAfter(date) || date.isAfter(startDate.plusDays(6));
        if (isDateOutOfRange) {
            throw ResponseError.BadRequest.INVALID_DATE.getResponseException();
        }

        ChallengeStatistics challengeStatistics =
                challengeStatisticsRepository.findByChallengeAndYearAndMonth(userChallenge.getChallenge(), date.getYear(), date.getMonthValue())
                        .orElseGet(() -> com.mozi.moziserver.model.entity.ChallengeStatistics.builder()
                                .challenge(userChallenge.getChallenge())
                                .year(date.getYear())
                                .month(date.getMonthValue())
                                .build());

        challengeStatistics.setPlayerConfirmCnt(challengeStatistics.getPlayerConfirmCnt() + 1);

        Optional<ChallengeStatisticsUserUniqCheck> userUniqCheck =
                challengeStatisticsUserUniqCheckRepository.findByChallengeAndYearAndMonthAndUser(userChallenge.getChallenge(), date.getYear(), date.getMonthValue(), userChallenge.getUser());

        // TODO 테이블에 유니크키 걸기
        if (!userUniqCheck.isPresent() && userChallenge.getTotalConfirmCnt() == 1) {
            challengeStatistics.setPlayerFirstTryingCnt(challengeStatistics.getPlayerFirstTryingCnt() + 1);

            ChallengeStatisticsUserUniqCheck uniqCheck = ChallengeStatisticsUserUniqCheck.builder()
                    .challenge(userChallenge.getChallenge())
                    .year(date.getYear())
                    .month(date.getMonthValue())
                    .user(userChallenge.getUser())
                    .build();

            challengeStatisticsUserUniqCheckRepository.save(uniqCheck);
        }

        challengeStatisticsRepository.save(challengeStatistics);
    }

    // TODO ScheduleService 에서 자동으로 상태가 변경될때 해당 유저챌린지의 포인트를 user_reward 포인트에 더해준다.

    // TODO 유저챌린지 그만두기를 했을때 추가 포인트를 user_reward 포인트에 더해준다.


    @Transactional
    public void stopUserChallenge(Long userSeq, Long userChallengeSeq) {
        LocalDate today = LocalDate.now();
        UserChallenge userChallenge = getUserChallenge(userSeq, userChallengeSeq);

        if (userChallenge.getState().equals(UserChallengeStateType.STOP)){
            throw ResponseError.BadRequest.ALREADY_STOPPED_USER_CHALLENGE.getResponseException();
        }

        if (!UserChallengeStateType.activeTypes.contains(userChallenge.getState())) {
            throw ResponseError.BadRequest.ALREADY_ENDED_USER_CHALLENGE.getResponseException();
        }

        userChallenge.setState(UserChallengeStateType.STOP);
        userChallenge.setEndDate(today);
        userChallenge.setCheckedState(true);

        userChallengeRepository.save(userChallenge);
    }

    @Transactional
    public void checkUserChallenge(Long userSeq, Long userChallengeSeq) {
        UserChallenge userChallenge = getUserChallenge(userSeq, userChallengeSeq);

        if (UserChallengeStateType.activeTypes.contains(userChallenge.getState())) {
            throw ResponseError.BadRequest.INVALID_USER_CHALLENGE.getResponseException();
        }

        userChallenge.setCheckedState(true);

        userChallengeRepository.save(userChallenge);
    }

    @Transactional
    public void updateUserChallengeStartDate(Long userSeq, Long userChallengeSeq, LocalDate newStartDate) {
        UserChallenge userChallenge = getUserChallenge(userSeq, userChallengeSeq);

        if (!UserChallengeStateType.activeTypes.contains(userChallenge.getState())) {
            throw ResponseError.BadRequest.ALREADY_ENDED_USER_CHALLENGE.getResponseException();
        }

        LocalDate today = LocalDate.now();

        if (userChallenge.getState() == UserChallengeStateType.PLAN
                && (newStartDate.equals(today) || newStartDate.isBefore(today))) {
            userChallenge.setState(UserChallengeStateType.DOING);
        }

        userChallenge.setStartDate(newStartDate);
        userChallenge.setEndDate(newStartDate.plusDays(6));

        userChallengeRepository.save(userChallenge);
    }

    @Transactional
    public void updateUserChallengeRecord(
            Challenge challenge,
            User user,
            Integer confirmCount,
            Integer points
    ) {
        UserChallengeRecord userChallengeRecord = userChallengeRecordRepository.findByChallengeAndUser(challenge.getSeq(), user.getSeq())
                .orElseGet(() -> UserChallengeRecord.builder()
                        .challenge(challenge)
                        .user(user)
                        .build());

        userChallengeRecord.setConfirmCnt(userChallengeRecord.getConfirmCnt() + 1);
        userChallengeRecord.setAcquisitionPoint(userChallengeRecord.getAcquisitionPoint() + points);

        userChallengeRecordRepository.save(userChallengeRecord);
    }

    public UserChallengeRecord getUserChallengeRecord(
            Long userSeq,
            Challenge challenge
    ) {
        User user = userRepository.getById(userSeq);

        UserChallengeRecord userChallengeRecord = userChallengeRecordRepository.findByChallengeAndUser(challenge.getSeq(), userSeq)
                .orElseGet(() -> UserChallengeRecord.builder()
                        .challenge(challenge)
                        .user(user)
                        .build());

        return userChallengeRecord;
    }

    public List<UserChallengeRecord> getUserChallengeRecordListByUserSeq(
            Long userSeq,
            ReqList reqList
    ) {

        return userChallengeRecordRepository.findByUserAndConfirmCnt(userSeq,reqList.getPrevLastSeq(),reqList.getPageSize());

    }
}
