package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.UserChallengeResultType;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserChallengeRepositorySupport {
    Optional<UserChallenge> findBySeq(Long seq);

    Optional<UserChallenge> findUserChallengeByUserSeqAndChallengeAndStates(Long userSeq, Challenge challenge, Collection<UserChallengeStateType> states);

    List<UserChallenge> findAllByUserSeq(
            Long userSeq,
            Integer pageSize,
            Long prevLastUserChallengeSeq
    );

    List<UserChallenge> findAllByPeriod(
            Long userSeq,
            LocalDate startDate,
            LocalDate endDate,
            Long challengeSeq
    );

    List<UserChallenge> findAllByStateAndCheckedState(
            Long userSeq
    );

    List<UserChallenge> findAllByPlanResult(
            LocalDate date,
            UserChallengeResultType planResult
    );

    Long updateState(
            LocalDate date,
            UserChallengeStateType beforeState,
            UserChallengeStateType afterState
    );

    List<UserChallenge> findAllByStateAndStartDate(UserChallengeStateType stateType, LocalDate startDate);
}
