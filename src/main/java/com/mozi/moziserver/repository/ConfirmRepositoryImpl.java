package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.ConfirmStateType;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


public class ConfirmRepositoryImpl extends QuerydslRepositorySupport implements ConfirmRepositorySupport {
    private final QConfirm qConfirm = QConfirm.confirm;
    private final QUser qUser = QUser.user;
    private final QChallenge qChallenge = QChallenge.challenge;
    private final QConfirmSticker qConfirmSticker = QConfirmSticker.confirmSticker;

    @PersistenceContext
    private EntityManager entityManager;


    public ConfirmRepositoryImpl() {
        super(Confirm.class);
    }

    @Override
    public List<Confirm> findAll(Long prevLastConfirmSeq, Integer pageSize) {
        Predicate[] predicates = new Predicate[]{
                predicateOptional(qConfirm.seq::lt, prevLastConfirmSeq),
                qConfirm.state.notIn(ConfirmStateType.DELETED, ConfirmStateType.BLOCKED)
        };

        List<Long> confirmSeqList = from(qConfirm)
                .select(qConfirm.seq)
                .where(predicates)
                .orderBy(qConfirm.seq.desc())
                .limit(pageSize)
                .fetch();

        return from(qConfirm)
                .innerJoin(qConfirm.challenge, qChallenge).fetchJoin()
                .innerJoin(qConfirm.user, qUser).fetchJoin()
                .leftJoin(qConfirm.confirmStickerList, qConfirmSticker).fetchJoin()
                .where(qConfirm.seq.in(confirmSeqList))
                .orderBy(qConfirm.seq.desc())
                .fetch();
    }

    @Override
    public List<Confirm> findAllByChallenge(Challenge challenge, Long prevLastConfirmSeq, Integer pageSize) {
        Predicate[] predicates = new Predicate[]{
                predicateOptional(qConfirm.seq::lt, prevLastConfirmSeq),
                predicateOptional(qConfirm.challenge::eq, challenge), // TODO change parameter non optional
                qConfirm.state.notIn(ConfirmStateType.DELETED, ConfirmStateType.BLOCKED)
        };

        List<Long> confirmSeqList = from(qConfirm)
                .select(qConfirm.seq)
                .where(predicates)
                .orderBy(qConfirm.createdAt.desc())
                .limit(pageSize)
                .fetch();

        return from(qConfirm)
                .innerJoin(qConfirm.challenge, qChallenge).fetchJoin()
                .innerJoin(qConfirm.user, qUser).fetchJoin()
                .leftJoin(qConfirm.confirmStickerList, qConfirmSticker).fetchJoin()
                .where(qConfirm.seq.in(confirmSeqList))
                .orderBy(qConfirm.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Confirm> findByUserByOrderDesc(User user, Long prevLastConfirmSeq, Integer pageSize) {
        Predicate[] predicates = new Predicate[]{
                predicateOptional(qConfirm.seq::lt, prevLastConfirmSeq),
                qUser.eq(user),
                qConfirm.state.notIn(ConfirmStateType.DELETED, ConfirmStateType.BLOCKED)
        };

        return from(qConfirm)
                .innerJoin(qConfirm.challenge, qChallenge).fetchJoin()
                .innerJoin(qConfirm.user, qUser).fetchJoin()
                .where(predicates)
                .orderBy(qConfirm.createdAt.desc())
                .limit(pageSize)
                .fetch();
    }

    @Override
    public Confirm findBySeq(Long seq) {

        return from(qConfirm)
                .where(qConfirm.seq.in(seq))
                .fetchOne();
    }

    @Transactional
    @Override
    public void updateStateSupportedCnt(Confirm confirm, ConfirmStateType state, Integer cnt) {

        update(qConfirm)
                .set(qConfirm.state, state)
                .set(qConfirm.reportedCnt, cnt)
                .where(qConfirm.eq(confirm))
                .execute();
    }

    @Override
    public Optional<Confirm> findByChallenge(Challenge challenge) {

        return from(qConfirm)
                .select(qConfirm)
                .where(qConfirm.challenge.eq(challenge))
                .where(qConfirm.state.notIn(ConfirmStateType.DELETED, ConfirmStateType.BLOCKED))
                .orderBy(qConfirm.createdAt.asc())
                .fetch()
                .stream()
                .distinct()
                .findFirst();
    }

    @Override
    public List<Confirm> findByCreatedAt(LocalDateTime localDateTime) {

        return from(qConfirm)
                .innerJoin(qConfirm.challenge, qChallenge).fetchJoin()
                .innerJoin(qConfirm.user, qUser).fetchJoin()
                .where(qConfirm.createdAt.goe(localDateTime))
                .where(qConfirm.state.notIn(ConfirmStateType.DELETED, ConfirmStateType.BLOCKED))
                .fetch();
    }

    @Override
    public List<Confirm> findByUserAndPeriod(User user, Challenge challenge, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Predicate[] predicates = new Predicate[]{
                qConfirm.createdAt.goe(startDateTime),
                qConfirm.createdAt.lt(endDateTime),
                qConfirm.user.eq(user),
                qConfirm.challenge.eq(challenge),
                qConfirm.state.notIn(ConfirmStateType.DELETED, ConfirmStateType.BLOCKED)
        };

        return from(qConfirm)
                .innerJoin(qConfirm.challenge, qChallenge).fetchJoin()
                .innerJoin(qConfirm.user, qUser).fetchJoin()
                .where(predicates)
                .fetch();
    }

    @Override
    public List<Confirm> findByPeriodAndPaging(LocalDateTime startDateTime, Long prevLastConfirmSeq, Integer pageSize) {
        Predicate[] predicates = new Predicate[]{
                qConfirm.createdAt.goe(startDateTime),
                predicateOptional(qConfirm.seq::lt, prevLastConfirmSeq),
                qConfirm.state.notIn(ConfirmStateType.DELETED, ConfirmStateType.BLOCKED)
        };

        return from(qConfirm)
                .leftJoin(qConfirm.challenge, qChallenge).fetchJoin() // TODO change to inner join
                .where(predicates)
                .orderBy(qConfirm.seq.desc())
                .limit(pageSize)
                .fetch();
    }

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    @Override
    public List<Confirm> findAllByUserSeqAndState(Long userSeq, List<ConfirmStateType> confirmSateList, Integer pageNumber, Integer pageSize) {
        Predicate[] predicates = new Predicate[]{
                predicateOptional(qUser.seq::eq, userSeq),
                predicateOptional(qConfirm.state::in, confirmSateList)
        };

        return from(qConfirm)
                .innerJoin(qConfirm.challenge, qChallenge).fetchJoin()
                .innerJoin(qConfirm.user, qUser).fetchJoin()
                .where(predicates)
                .orderBy(qConfirm.createdAt.desc())
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetch();
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}