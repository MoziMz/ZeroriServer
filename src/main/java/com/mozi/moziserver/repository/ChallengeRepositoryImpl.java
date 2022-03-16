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

//    @Override
//    public List<Challenge> findAll (
//            Long userSeq,
////            List<String> tagType,
//            List<String> themeType,
//            Integer pageSize,
//            Long prevLastPostSeq
//    ) {
//        final Predicate[] predicates = new Predicate[]{
//                predicateOptional(qChallenge.seq::lt,prevLastPostSeq),
//        };
//
//        List<ChallengeTagType> tags = new ArrayList<>();
//        List<ChallengeThemeType> themes = new ArrayList<>();
//
//        tags = (tagType != null) ? setTags(tags, tagType) : null;
//        themes = (themeType != null) ? setThemes(themes, themeType) : null;
//
//        List<Challenge> challengeList = from(qChallenge)
//                .leftJoin(qChallengeTheme).on(qChallenge.seq.eq(qChallengeTheme.id.challenge.seq))
//                .leftJoin(qChallengeTag).on(qChallenge.seq.eq(qChallengeTag.id.challenge.seq))
//                .where(predicates)
//                .where(
//                        tagNameIn(tags),
//                        themeNameIn(themes)
//                )
//                .groupBy(qChallenge.seq)
//                .orderBy(qChallenge.seq.asc())
//                .limit(pageSize)
//                .fetch()
//                .stream()
//                .collect(Collectors.toList());
//
//        return challengeList;
//
//    }

    @Override
    public List<Challenge> findAll (
            Long userSeq,
            List<String> tagType,
            List<String> themeType,
            Integer pageSize,
            Long prevLastPostSeq
    ) {
        List<Challenge> challengeList = from(qChallenge)
                .limit(pageSize)
                .fetch()
                .stream()
                .collect(Collectors.toList());

        return challengeList;
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }

    private List<ChallengeTagType> setTags(List<ChallengeTagType> tags, List<String> tagType) {
        for (int i = 0; i < tagType.size(); i++) {
            tags.add(ChallengeTagType.valueOf(tagType.get(i)));
        }
        return tags;
    }

    private List<ChallengeThemeType> setThemes(List<ChallengeThemeType> themes, List<String> themeType) {
        for (int i = 0; i < themeType.size(); i++) {
            themes.add(ChallengeThemeType.valueOf(themeType.get(i)));
        }
        return themes;
    }

//    private BooleanExpression tagNameIn(List<ChallengeTagType> tags) {
//        return tags != null ? qChallengeTag.id.tagName.in(tags) : null;
//    }

    private BooleanExpression themeNameIn(List<ChallengeThemeType> themes) {
        return themes != null ? qChallengeTheme.id.themeName.in(themes) : null;
    }
}
