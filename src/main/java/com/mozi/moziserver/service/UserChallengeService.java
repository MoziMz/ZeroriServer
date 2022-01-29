package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.PlanDateResultType;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import com.mozi.moziserver.model.req.*;
import com.mozi.moziserver.repository.ChallengeRepository;
import com.mozi.moziserver.repository.UserChallengeRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserChallengeService {
    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;

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

        // TODO
        // startDate 가 과거이면 ERROR
        LocalDate today = LocalDate.now();

        if ( req.getStartDate().isBefore(today) ) {
            throw ResponseError.BadRequest.PAST_START_DATE.getResponseException();
        }

        UserChallengeStateType stateType = UserChallengeStateType.PLAN;
        if ( req.getStartDate().isEqual(today) ) {
            stateType = UserChallengeStateType.DOING;
        }

        if( req.getPlanDates().size() != 7 ) {
            throw ResponseError.BadRequest.PAST_START_DATE.getResponseException();
        }

        for( int i = 0; i < 7; i++ ) {
            ReqUserChallengePlanDate planDate = req.getPlanDates().get(i);
            if( planDate.getTurn() != i + 1 ) {
                throw ResponseError.BadRequest.PAST_START_DATE.getResponseException();
            }
        }

        long count = req.getPlanDates().stream()
                .map(plandate -> plandate.getTurn())
                .filter( turn -> turn >= 1 && turn <= 7)
                .distinct()
                .count();
        if( count != 7 ) {
            throw ResponseError.BadRequest.INVALID_TURN.getResponseException();
        }

        req.getPlanDates()
                .sort(Comparator.comparingInt(ReqUserChallengePlanDate::getTurn));

        for ( ReqUserChallengePlanDate planDate :  req.getPlanDates() ) {
            PlanDateResultType type = planDate.getPlanDateType();
            if( type == PlanDateResultType.FAIL || type == PlanDateResultType.SUCCESS ) {
                throw ResponseError.BadRequest.PLAN_DATE_TYPE_NOT_INVALID.getResponseException();
            }
        }

        final UserChallenge userChallenge = UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .startDate(req.getStartDate())
                .PlanDateList(req.getPlanDates().stream()
                                .sorted(Comparator.comparingInt(ReqUserChallengePlanDate::getTurn))
                                .map(ReqUserChallengePlanDate::toPlanDate)
                                .collect(Collectors.toList()))
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

        UserChallenge curUserChallenge = userChallengeRepository.findById(userChallengeSeq)
                .orElseThrow(ResponseError.NotFound.USER_CHALLENGE_NOT_EXISTS::getResponseException);

        if (curUserChallenge.getState() == UserChallengeStateType.END) {
            throw ResponseError.BadRequest.ALREADY_ENDED_USER_CHALLENGE.getResponseException();
        }

        long count = req.getPlanDates().stream()
                .map(plandate -> plandate.getTurn())
                .filter( turn -> turn >= 1 && turn <= 7)
                .distinct()
                .count();
        if( count != 7 ) {
            throw ResponseError.BadRequest.INVALID_TURN.getResponseException();
        }

        req.getPlanDates()
                .sort(Comparator.comparingInt(ReqUserChallengePlanDate::getTurn));

        LocalDate today = LocalDate.now();
        LocalDate startDate = req.getStartDate();

        Period period = Period.between(today, startDate);
        int updatableFirstTurn = today.isAfter(startDate) ? period.getDays() : 0;

        if (today.isAfter(startDate)) {
            int todayTurn = updatableFirstTurn;
            for (int i = 0; i < todayTurn; i++) {
                PlanDateResultType curPlanDateResult = curUserChallenge.getPlanDateList().get(i).getResult();
                PlanDateResultType reqPlanDateResult = req.getPlanDates().get(i).getPlanDateType();

                if (curPlanDateResult != reqPlanDateResult) {
                    throw ResponseError.BadRequest.PAST_DATES_UPDATE_NOT_INVALID.getResponseException();
                }
            }
        }

        for ( int i = updatableFirstTurn; i < 7; i++ ) {
            PlanDateResultType type = req.getPlanDates().get(i).getPlanDateType();
            if( type == PlanDateResultType.FAIL || type == PlanDateResultType.SUCCESS ) {
                throw ResponseError.BadRequest.PLAN_DATE_TYPE_NOT_INVALID.getResponseException();
            }
        }

        UserChallengeStateType stateType = UserChallengeStateType.PLAN;
        if (req.getStartDate().isEqual(today)) {
            stateType = UserChallengeStateType.DOING;
        }

        final UserChallenge userChallenge = UserChallenge.builder()
                .user(curUserChallenge.getUser())
                .challenge(curUserChallenge.getChallenge())
                .startDate(req.getStartDate())
                .PlanDateList(req.getPlanDates().stream()
                        .map(ReqUserChallengePlanDate::toPlanDate)
                        .collect(Collectors.toList()))
                .state(stateType)
                .build();

        userChallengeRepository.save(userChallenge);
    }
}
