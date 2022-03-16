package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.entity.UserChallengeRecord;

import java.util.Optional;

public interface UserChallengeRecordRepositorySupport {
    Optional<UserChallengeRecord> findByChallengeAndUser(Long challengeSeq, Long userSeq);
}
