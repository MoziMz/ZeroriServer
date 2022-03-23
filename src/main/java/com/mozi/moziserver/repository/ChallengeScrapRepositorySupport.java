package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ChallengeScrap;

import java.util.Optional;

public interface ChallengeScrapRepositorySupport {
    ChallengeScrap findByChallengeSeqAndUserSeq (Long challengeSeq, Long userSeq );
}
