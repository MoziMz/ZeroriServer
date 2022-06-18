package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public void updateAcquisitionPoint(
            Long challengeSeq,
            Long userSeq,
            Integer point
    ) {

        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qUserChallengeRecord.challenge.seq::eq, challengeSeq),
                predicateOptional(qUserChallengeRecord.user.seq::eq, userSeq)
        };

        update(qUserChallengeRecord)
                .set(qUserChallengeRecord.acquisitionPoint, qUserChallengeRecord.acquisitionPoint.add(point))
                .where(predicates)
                .execute();

        return;
    }

    @Override
    public List<UserChallengeRecord> findByUser(Long userSeq,Long prevLastChallengeSeq, Integer pageSize){
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qUserChallengeRecord.challenge.seq::lt,prevLastChallengeSeq),
        };

        return from(qUserChallengeRecord)
                .where(qUser.seq.eq(userSeq))
                .orderBy(qChallenge.name.asc())
                .where(predicates)
                .limit(pageSize)
                .fetch()
                .stream()
                .collect(Collectors.toList());
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }

}
