package com.mozi.moziserver.repository;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import com.mozi.moziserver.model.mappedenum.ChallengeThemeType;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChallengeRepositoryImpl extends QuerydslRepositorySupport implements ChallengeRepositorySupport {
    private final QChallenge qChallenge = QChallenge.challenge;
    private final QChallengeTag qChallengeTag = QChallengeTag.challengeTag;
    private final QChallengeTheme qChallengeTheme = QChallengeTheme.challengeTheme;
    private final QChallengeRecord qChallengeRecord = QChallengeRecord.challengeRecord;

    public ChallengeRepositoryImpl() {
        super(Challenge.class);
    }

    @Override
    public Optional<Challenge> findBySeq (Long seq ) {
        return from(qChallenge)
                .innerJoin(qChallengeRecord)
                .on(qChallenge.seq.eq(qChallengeRecord.challenge.seq))
                .fetchJoin()
                .where(qChallenge.seq.eq(seq))
                .fetch()
                .stream()
                .distinct()
                .findFirst();
    }

    @Override
    public List<Challenge> findAll (
            Long userSeq,
            List<ChallengeTagType> tagTypeList,
            List<Long> themeSeqList,
            Integer pageSize,
            Long prevLastPostSeq
    ) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qChallenge.seq::lt,prevLastPostSeq),
                predicateOptional(qChallenge.themeSeq::in,themeSeqList),
                predicateOptional(qChallenge.mainTag::in, tagTypeList)
        };

        List<Challenge> challengeList = from(qChallenge)
                .where(predicates)
                .orderBy(qChallenge.seq.desc())
                .limit(pageSize)
                .fetch();

        return challengeList;
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}
