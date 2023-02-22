package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeScrap;
import com.mozi.moziserver.model.entity.QChallengeScrap;
import com.mozi.moziserver.model.entity.User;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.function.Function;

public class ChallengeScrapRepositoryImpl extends QuerydslRepositorySupport implements ChallengeScrapRepositorySupport {
    private final QChallengeScrap qChallengeScrap = QChallengeScrap.challengeScrap;

    public ChallengeScrapRepositoryImpl() {
        super(ChallengeScrap.class);
    }

    @Override
    public ChallengeScrap findByChallengeAndUser(Challenge challenge, User user) {
        return from(qChallengeScrap)
                .where(qChallengeScrap.challenge.eq(challenge)
                        .and(qChallengeScrap.user.eq(user)))
                .fetchOne();
    }

    @Override
    public List<ChallengeScrap> findByUser(User user) {
        return from(qChallengeScrap)
                .where(qChallengeScrap.user.eq(user))
                .orderBy(qChallengeScrap.createdAt.desc())
                .fetch();
    }

    @Override
    public List<ChallengeScrap> findByUser(User user, Long prevChallengeScrapSeq, int pageSize) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qChallengeScrap.seq::lt, prevChallengeScrapSeq),
                qChallengeScrap.user.eq(user)
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
