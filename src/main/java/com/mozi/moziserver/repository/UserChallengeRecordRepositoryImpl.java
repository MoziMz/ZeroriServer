package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Optional;
import java.util.function.Function;

public class UserChallengeRecordRepositoryImpl extends QuerydslRepositorySupport implements UserChallengeRecordRepositorySupport {
    private final QUserChallengeRecord qUserChallengeRecord = QUserChallengeRecord.userChallengeRecord;
    private final QChallenge qChallenge = QChallenge.challenge;
    private final QUser qUser = QUser.user;

    public UserChallengeRecordRepositoryImpl() { super(UserChallengeRecord.class); }

    public Optional<UserChallengeRecord> findByChallengeAndUser(Long challengeSeq, Long userSeq) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qUserChallengeRecord.challenge.seq::eq, challengeSeq),
                predicateOptional(qUserChallengeRecord.user.seq::eq, userSeq)
        };

        return from(qUserChallengeRecord)
                .innerJoin(qChallenge)
                .on(qUserChallengeRecord.challenge.seq.eq(qChallenge.seq))
                .fetch()
                .stream()
                .findFirst();
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}
