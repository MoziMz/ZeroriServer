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
        return Optional.ofNullable(from(qChallenge)
                .innerJoin(qChallengeRecord)
                .on(qChallenge.seq.eq(qChallengeRecord.challenge.seq))
                .fetchJoin()
                .where(qChallenge.seq.eq(seq))
                .fetchFirst());
    }

    @Override
    public List<Challenge> findAll (
            Long userSeq,
            List<Long> tagSeqList,
            List<Long> themeSeqList,
            String keyword,
            Integer pageSize,
            Long prevLastPostSeq
    ) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qChallenge.seq::lt, prevLastPostSeq),
                predicateOptional(qChallenge.themeSeq::in, themeSeqList),
                predicateOptional(qChallengeTag.tag.seq::in, tagSeqList),
                keyword != null ? predicateOptional(qChallenge.name::like, '%' + keyword + '%') : null
        };

        return from(qChallenge)
                .innerJoin(qChallengeTag).on(qChallenge.seq.eq(qChallengeTag.challenge.seq)).fetchJoin()
                .where(predicates)
                .orderBy(qChallenge.seq.desc())
                .limit(pageSize)
                .fetch();
    }

    @Override
    public long countChallengeList(
            List<Long> tagSeqList,
            List<Long> themeSeqList,
            String keyword
    ){
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qChallenge.themeSeq::in, themeSeqList),
                predicateOptional(qChallengeTag.tag.seq::in, tagSeqList),
                keyword != null ? predicateOptional(qChallenge.name::like, '%' + keyword + '%') : null
        };

        return from(qChallenge)
                .innerJoin(qChallengeTag).on(qChallenge.seq.eq(qChallengeTag.challenge.seq)).fetchJoin()
                .where(predicates)
                .orderBy(qChallenge.seq.desc())
                .fetchCount();
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}
