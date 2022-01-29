package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.QChallenge;
import com.mozi.moziserver.model.entity.QUserChallenge;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserChallengeRepositoryImpl extends QuerydslRepositorySupport implements UserChallengeRepositorySupport {
    private final QUserChallenge qUserChallenge = QUserChallenge.userChallenge;
    private final QChallenge qChallenge = QChallenge.challenge;

    public UserChallengeRepositoryImpl() { super(UserChallenge.class); }

    @Override
    public Optional<UserChallenge> findUserChallengeByUserSeqAndChallengeAndStates(Long userSeq, Challenge challenge, Collection<UserChallengeStateType> states){

        return from(qUserChallenge)
                .where(qUserChallenge.user.seq.eq(userSeq)
                    .and(qUserChallenge.challenge.eq(challenge))
                    .and(qUserChallenge.state.in(states)))
                .fetch()
                .stream()
                .findFirst();
    }

    @Override
    public List<UserChallenge> findAllByUserSeq(
            Long userSeq,
            Integer pageSize,
            Long prevLastUserChallengeSeq
    ){

//        final Predicate[] predicates = new Predicate[]{
//                predicateOptional(qPost.seq::lt, prevLastPostSeq)
//        };
//
//        return from(qPost)
//                .select(new QPostProjection(qPost, qPostLike, qPostBookmark))
//                //.innerJoin(qPost.user, qUser).fetchJoin()
////                .leftJoin(qPost.pictures, qPostPicture).fetchJoin()
//                .leftJoin(qPostLike)
//                .on(qPostLike.postSeq.eq(qPost.seq)
//                        .and(qPostLike.userSeq.eq(userSeq)))
//                .leftJoin(qPostBookmark)
//                .on(qPostBookmark.postSeq.eq(qPost.seq)
//                        .and(qPostBookmark.userSeq.eq(userSeq)))
//                .where(predicates)
//                .where(qPost.user.seq.eq(userSeq))
//                .orderBy(qPost.seq.desc())
//                .limit(pageSize)
//                .fetch()
//                .stream()
//                .distinct()
//                .map(PostProjection::getPost)
//                .collect(Collectors.toList());

        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qUserChallenge.seq::lt, prevLastUserChallengeSeq),
                predicateOptional(qUserChallenge.user.seq::eq, userSeq)
        };

        return from(qUserChallenge)
                .innerJoin(qUserChallenge.challenge, qChallenge)
                    .fetchJoin()
                .where(predicates)
                .orderBy(qUserChallenge.startDate.desc())
                .limit(pageSize)
                .fetch()
                .stream()
                .distinct()
                .collect(Collectors.toList());

    }

    @Override
    public List<UserChallenge> findAllByPeriod(
            Long userSeq,
            LocalDate startDate,
            LocalDate endDate,
            Long challengeSeq
    ) {
        final Predicate[] predicates = new Predicate[]{
                qUserChallenge.user.seq.eq(userSeq),
                qUserChallenge.startDate.gt(startDate.minusDays(7)),
                qUserChallenge.startDate.loe(endDate),
                predicateOptional(qUserChallenge.challenge.seq::eq, challengeSeq)
        };

        return from(qUserChallenge)
                .innerJoin(qChallenge)
                .on(qUserChallenge.challenge.seq.eq(qChallenge.seq))
                .where(predicates)
                .orderBy(qUserChallenge.startDate.desc())
                .fetch()
                .stream()
                .distinct()
                .collect(Collectors.toList());


    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }

}
