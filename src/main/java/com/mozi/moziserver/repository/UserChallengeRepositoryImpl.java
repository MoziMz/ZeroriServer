package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.UserChallengeResultType;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserChallengeRepositoryImpl extends QuerydslRepositorySupport implements UserChallengeRepositorySupport {
    private final QUserChallenge qUserChallenge = QUserChallenge.userChallenge;
    private final QChallenge qChallenge = QChallenge.challenge;
    private final QUser qUser = QUser.user;

    private final QChallengeRecord qChallengeRecord = QChallengeRecord.challengeRecord;

    public UserChallengeRepositoryImpl() { super(UserChallenge.class); }

    @Override
    public Optional<UserChallenge> findBySeq(Long seq) {
        return Optional.ofNullable(from(qUserChallenge)
                .select(qUserChallenge)
                .innerJoin(qUserChallenge.challenge, qChallenge).fetchJoin()
                .innerJoin(qUserChallenge.user, qUser).fetchJoin()
                .where(qUserChallenge.seq.eq(seq))
                .fetchFirst());
    }



    @Override
    public Optional<UserChallenge> findUserChallengeByUserSeqAndChallengeAndStates(Long userSeq, Challenge challenge, Collection<UserChallengeStateType> states){

        return Optional.ofNullable(from(qUserChallenge)
                .where(qUserChallenge.user.seq.eq(userSeq)
                    .and(qUserChallenge.challenge.eq(challenge))
                    .and(qUserChallenge.state.in(states)))
                .orderBy(qUserChallenge.endDate.desc())
                .fetchFirst());
    }

    @Override
    public List<UserChallenge> findAllByUserSeq(
            Long userSeq,
            Integer pageSize,
            Long prevLastUserChallengeSeq
    ){
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qUserChallenge.seq::gt, prevLastUserChallengeSeq),
                predicateOptional(qUserChallenge.user.seq::eq, userSeq)
        };

        return from(qUserChallenge)
                .leftJoin(qUserChallenge.challenge,qChallenge).fetchJoin()
                .leftJoin(qChallenge.challengeRecord,qChallengeRecord).fetchJoin()
                .orderBy(qUserChallenge.startDate.asc())
                .where(predicates)
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
                predicateOptional(qUserChallenge.challenge.seq::eq, challengeSeq),
                qUserChallenge.state.notIn(UserChallengeStateType.STOP)
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

    @Override
    public List<UserChallenge> findAllByStateAndCheckedState(
            Long userSeq
    ) {
        final Predicate[] predicates = new Predicate[]{
                qUserChallenge.user.seq.eq(userSeq),
                qUserChallenge.checkedState.eq(false),
                qUserChallenge.state.eq(UserChallengeStateType.END)
        };

        return from(qUserChallenge)
                .innerJoin(qChallenge)
                .on(qUserChallenge.challenge.seq.eq(qChallenge.seq))
                .where(predicates)
                .fetch()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<UserChallenge> findAllByPlanResult(
            LocalDate date,
            UserChallengeResultType planResult
    ) {
        // startDate > date-7 &&startDate <= date
        final Predicate[] predicates = new Predicate[]{
                qUserChallenge.state.eq(UserChallengeStateType.DOING),
                qUserChallenge.startDate.gt(date.minusDays(7)),
                qUserChallenge.startDate.loe(date)
        };

        return from(qUserChallenge)
                .where(predicates)
                .fetch();
//                .stream()
//                .filter( item -> item.getUserChallengeResultList().get(Period.between(item.getStartDate(), date).getDays()).getResult() == UserChallengeResultType.PLAN)
//                .collect(Collectors.toList());

        // TODO
        // filter 에 걸려서 나오지 않는데도 JSON 내용이 바뀐것으로 인식되고 있음
        // update 실행됨
    }

    @Transactional
    @Override
    public Long updateState(
            LocalDate date,
            UserChallengeStateType beforeState,
            UserChallengeStateType afterState
    ) {
        final Predicate[] predicates = new Predicate[]{
                qUserChallenge.state.eq(beforeState),
                qUserChallenge.startDate.eq(date)
        };

        long resultCount = update(qUserChallenge)
                .set(qUserChallenge.state, afterState)
                .where(predicates)
                .execute();

        return resultCount;
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }

}
