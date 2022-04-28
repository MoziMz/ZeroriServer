package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ChallengeStatistics;
import com.mozi.moziserver.model.entity.QChallenge;
import com.mozi.moziserver.model.entity.QChallengeStatistics;
import com.querydsl.core.types.Predicate;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChallengeStatisticsRepositoryImpl extends QuerydslRepositorySupport implements ChallengeStatisticsRepositorySupport {
    private final QChallengeStatistics qChallengeStatistics = QChallengeStatistics.challengeStatistics;
    private final QChallenge qChallenge = QChallenge.challenge;

    public ChallengeStatisticsRepositoryImpl() { super(ChallengeStatistics.class); }

    @Override
    public List<ChallengeStatistics> findAllByPeriod(
            Long challengeSeq,
            Integer startYear,
            Integer startMonth,
            Integer endYear,
            Integer endMonth
    ) {

        final Predicate[] predicates = new Predicate[]{
                qChallengeStatistics.challenge.seq.eq(challengeSeq),
                (qChallengeStatistics.year.eq(startYear).and(qChallengeStatistics.month.goe(startMonth)))
                        .or(qChallengeStatistics.year.gt(startYear).and(qChallengeStatistics.year.lt(endYear)))
                        .or(qChallengeStatistics.year.eq(endYear).and(qChallengeStatistics.month.loe(endMonth)))
        };

        return from(qChallengeStatistics)
                .where(predicates)
                .fetch()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}
