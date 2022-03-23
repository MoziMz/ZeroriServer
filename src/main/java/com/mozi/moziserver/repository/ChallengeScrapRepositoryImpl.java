package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Optional;

public class ChallengeScrapRepositoryImpl extends QuerydslRepositorySupport implements ChallengeScrapRepositorySupport{
    private final QChallengeScrap qChallengeScrap=QChallengeScrap.challengeScrap;

    public ChallengeScrapRepositoryImpl() {
        super(ChallengeScrap.class);
    }

    @Override
    public ChallengeScrap findByChallengeSeqAndUserSeq (Long challengeSeq,Long userSeq ) {
        return from(qChallengeScrap)
                .where(qChallengeScrap.challengeSeq.eq(challengeSeq)
                .and(qChallengeScrap.userSeq.eq(userSeq)))
                .fetchOne();
    }
}
