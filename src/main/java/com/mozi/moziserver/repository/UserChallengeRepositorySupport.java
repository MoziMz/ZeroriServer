package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;

import java.util.Collection;
import java.util.Optional;

public interface UserChallengeRepositorySupport {

    Optional<UserChallenge> findUserChallengeByUserSeqAndChallengeAndStates(Long userSeq, Challenge challenge, Collection<UserChallengeStateType> states);
}
