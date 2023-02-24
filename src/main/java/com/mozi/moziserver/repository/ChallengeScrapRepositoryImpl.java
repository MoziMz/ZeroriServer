package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.ChallengeStateType;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.function.Function;

public class ChallengeScrapRepositoryImpl extends QuerydslRepositorySupport implements ChallengeScrapRepositorySupport {
    private final QChallengeScrap qChallengeScrap = QChallengeScrap.challengeScrap;
    private final QChallenge qChallenge= QChallenge.challenge;

    public ChallengeScrapRepositoryImpl() {
        super(ChallengeScrap.class);
    }

    @Override
    public ChallengeScrap findByChallengeAndUser(Challenge challenge, User user) {
        final Predicate[] predicates = new Predicate[]{
                qChallengeScrap.challenge.eq(challenge),
                qChallengeScrap.user.eq(user),
                qChallenge.state.ne(ChallengeStateType.DELETED)
        };

        return from(qChallengeScrap)
                .where(predicates)
                .fetchOne();
    }

    @Override
    public List<ChallengeScrap> findByUser(User user) {
        final Predicate[] predicates = new Predicate[]{
                qChallengeScrap.user.eq(user),
                qChallenge.state.ne(ChallengeStateType.DELETED)
        };

        return from(qChallengeScrap)
                .where(predicates)
                .orderBy(qChallengeScrap.createdAt.desc())
                .fetch();
    }

    @Override
    public List<ChallengeScrap> findByUser(User user, Long prevChallengeScrapSeq, int pageSize) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qChallengeScrap.seq::lt, prevChallengeScrapSeq),
                qChallengeScrap.user.eq(user),
                qChallenge.state.ne(ChallengeStateType.DELETED)
        };

        return from(qChallengeScrap)
                .where(predicates)
                .orderBy(qChallengeScrap.seq.desc())
                .limit(pageSize)
                .fetch();
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}
