package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.res.ResChallengeTagList;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public List<ChallengeScrap> findByUserSeq(Long userSeq){
        return from(qChallengeScrap)
                .where(qChallengeScrap.userSeq.eq(userSeq))
                .orderBy(qChallengeScrap.createdAt.desc())
                .fetch();
    }
}
