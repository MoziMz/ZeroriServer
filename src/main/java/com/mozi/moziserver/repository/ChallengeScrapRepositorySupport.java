package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeScrap;
import com.mozi.moziserver.model.entity.User;

import java.util.List;

public interface ChallengeScrapRepositorySupport {
    ChallengeScrap findByChallengeAndUser(Challenge challenge, User user);

    List<ChallengeScrap> findByUser(User user);

    List<ChallengeScrap> findByUser(User user, Long prevChallengeScrapSeq, int pageSize);
}
