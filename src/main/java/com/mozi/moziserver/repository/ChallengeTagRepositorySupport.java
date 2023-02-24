package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.res.ResChallengeTagList;

import java.util.List;

public interface ChallengeTagRepositorySupport {
    List<ResChallengeTagList> findAllByChallengeSeq(Long seq);
}
