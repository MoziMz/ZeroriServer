package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.UserChallengeResultType;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import com.mozi.moziserver.model.req.*;
import com.mozi.moziserver.repository.*;
import lombok.RequiredArgsConstructor;
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

    private UserChallenge getUserChallenge (Long userSeq, Long userChallengeSeq) {
        UserChallenge userChallenge = userChallengeRepository.findById(userChallengeSeq)
                .orElseThrow(ResponseError.NotFound.USER_CHALLENGE_NOT_EXISTS::getResponseException);

        if (!userChallenge.getUser().getSeq().equals(userSeq)) {
            throw ResponseError.Forbidden.NO_AUTHORITY.getResponseException();
        }

        return userChallenge;
    }

    public Optional<UserChallenge> getActiveUserChallenge(Long userSeq, Challenge challenge ) {

        return userChallengeRepository.findUserChallengeByUserSeqAndChallengeAndStates(userSeq, challenge, UserChallengeStateType.activeTypes);
//                .orElseThrow(ResponseError.NotFound.USER_CHALLENGE_NOT_EXISTS::getResponseException);

//        return  userChallenge;
    }

    public List<UserChallenge> getUserChallengeList(Long userSeq, ReqList req) {
        return userChallengeRepository.findAllByUserSeq(
                userSeq,
                req.getPageSize(),
                req.getPrevLastPostSeq()
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

    @Transactional
    public void createUserChallenge(Long userSeq, ReqUserChallengeCreate req) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Challenge challenge = challengeRepository.findById(req.getChallengeSeq())
                .orElseThrow(ResponseError.NotFound.CHALLENGE_NOT_EXISTS::getResponseException);

        boolean isExists = userChallengeRepository.findUserChallengeByUserSeqAndChallengeAndStates(userSeq, challenge, Arrays.asList(UserChallengeStateType.PLAN, UserChallengeStateType.DOING))
                .isPresent();
        if ( isExists ) {
            throw ResponseError.BadRequest.ALREADY_EXISTS_USER_CHALLENGE_IN_PROGRESS.getResponseException();
        }

        LocalDate today = LocalDate.now();

        if ( req.getStartDate().isBefore(today) ) {
            throw ResponseError.BadRequest.PAST_START_DATE.getResponseException();
        }

        UserChallengeStateType stateType = UserChallengeStateType.PLAN;
        if ( req.getStartDate().isEqual(today) ) {
            stateType = UserChallengeStateType.DOING;
        }

        final UserChallenge userChallenge = UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .startDate(req.getStartDate())
                .state(stateType)
                .build();

        isExists = userChallengeRepository.findUserChallengeByUserSeqAndChallengeAndStates(userSeq, challenge, Arrays.asList(UserChallengeStateType.PLAN, UserChallengeStateType.DOING))
                .isPresent();
        if ( isExists ) {
            throw ResponseError.BadRequest.ALREADY_EXISTS_USER_CHALLENGE_IN_PROGRESS.getResponseException();
        }

        userChallengeRepository.save(userChallenge);

        // TODO
        // 첫번째 요청인지 확인하고 아니면 에러를 던진다.
    }

    public void updateUserChallenge(Long userSeq, Long userChallengeSeq, ReqUserChallengeUpdate req) {

        UserChallenge curUserChallenge = getUserChallenge(userSeq, userChallengeSeq);

        if (curUserChallenge.getState() == UserChallengeStateType.END) {
            throw ResponseError.BadRequest.ALREADY_ENDED_USER_CHALLENGE.getResponseException();
        }

        // TODO 기획 정책을 확인해서 정해야 할 부분
        // 어떤 시점에 startDate 수정이 가능한지 -> 시작일이 오늘일때, 아직 인증을 한번도 아닌 진행상태일때 (어제 시작되었어도) ..

        // startDate 가 오늘 이전이면 에러
        LocalDate today = LocalDate.now();
        LocalDate startDate = req.getStartDate();

        if( startDate.isBefore(today) ) {
            throw ResponseError.BadRequest.PAST_START_DATE.getResponseException();
        }

        if (curUserChallenge.getState() == UserChallengeStateType.PLAN && req.getStartDate().isEqual(today)) {
            UserChallengeStateType stateType = UserChallengeStateType.DOING;
            curUserChallenge.setState(stateType);
        }

        curUserChallenge.setStartDate(req.getStartDate());

        userChallengeRepository.save(curUserChallenge);
    }

    public void updateUserChallengeResult (UserChallenge userChallenge, LocalDate date, UserChallengeResultType result) {
       // date 가 startDate <= date <startDate+7 범위를 벗어나면 에러
        // date < startDate or date >= startDate+7
        LocalDate startDate = userChallenge.getStartDate();
        boolean isDateOutOfRange = startDate.isAfter(date) || date.isAfter(startDate.plusDays(6));
        if( isDateOutOfRange ) {
            throw ResponseError.BadRequest.INVALID_DATE.getResponseException();
        }

        // Result 를 None -> Complete 로 변경하는 것인지 Complete -> None 으로 변경하는 것인지 체크하기 위해
//        int confirmCntBeforeUpdate = (int) userChallenge.getResultList()
//                .stream()
//                .filter(c -> c.getResult().equals(UserChallengeResultType.COMPLETE))
//                .count();

        // date 에 해당하는 result 바꿔줌
        Period period = Period.between(date, startDate);
        Integer turn = period.getDays();

        userChallenge.getResultList().get(turn).setResult(result);

        // 인증 갯수를 업데이트 해준다.
        int confirmCnt = (int) userChallenge.getResultList()
                .stream()
                .filter(c -> c.getResult().equals(UserChallengeResultType.COMPLETE))
                .count();

        userChallenge.setTotalConfirmCnt(confirmCnt);

        int acquisitionPoints = (int) userChallenge.getChallenge().getPoint() * confirmCnt;

        userChallenge.setAcquisitionPoints(acquisitionPoints);

        UserReward userReward = userRewardRepository.findByUser(userChallenge.getUser())
                .orElseGet(() -> UserReward.builder()
                        .user(userChallenge.getUser())
                        .build());

        userReward.setPoint(userReward.getPoint() + userChallenge.getChallenge().getPoint());

        userRewardRepository.save(userReward);

        // 저장(업데이트)
        userChallengeRepository.save(userChallenge);

         updateChallengeStatisticsByUserChallenge(userChallenge, date);
    }

    private void updateChallengeStatisticsByUserChallenge ( UserChallenge userChallenge, LocalDate date ) {
        // TODO
        // 인증이 취소되었을 -1 되는 부분은 추후에 구현하기 ( 현재 인증을 했을때 +1 되는 부분에 대한 처리만 구현함 )

        // date 가 유저챌린지 범주에 해당하는지
        LocalDate startDate = userChallenge.getStartDate();
        boolean isDateOutOfRange = startDate.isAfter(date) || date.isAfter(startDate.plusDays(6));
        if( isDateOutOfRange ) {
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

        // player_first_trying_cnt 업데이트
        // 첫 인증 날짜에 해당하는 달에 관한 정보가 유니크 테이블에 존재하는지 확인
        // 없으면 만들어서 넣고
        // first_trying_cnt += 1 을 한다.
        Optional<ChallengeStatisticsUserUniqCheck> userUniqCheck =
                challengeStatisticsUserUniqCheckRepository.findByChallengeAndYearAndMonthAndUser(userChallenge.getChallenge(), date.getYear(), date.getMonthValue(), userChallenge.getUser());

        // 이번달의 첫 '인증하기' 이면 챌린지통계정보를 업데이트하고 유니크체크를 insert 한다.
        if ( !userUniqCheck.isPresent() && userChallenge.getTotalConfirmCnt() == 1) {
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

    // TODO
    // ScheduleService 에서 자동으로 상태가 변경될때 해당 유저챌린지의 포인트를 user_reward 포인트에 더해준다.

    // TODO
    // 유저챌린지 그만두기를 했을때 추가 포인트를 user_reward 포인트에 더해준다.
}
