package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.entity.QChallenge;
import com.mozi.moziserver.model.entity.QConfirm;
import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.mappedenum.ConfirmStateType;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;



public class ConfirmRepositoryImpl extends QuerydslRepositorySupport implements ConfirmRepositorySupport{
    private final QConfirm qConfirm = QConfirm.confirm;
    private final QUser qUser = QUser.user;
    private final QChallenge qChallenge = QChallenge.challenge;
    private final QConfirmSticker qConfirmSticker = QConfirmSticker.confirmSticker;

    @PersistenceContext
    private EntityManager entityManager;


    public ConfirmRepositoryImpl() { super(Confirm.class); }

    @Override
    public List<Confirm> findAll(Long prevLastConfirmSeq,Integer pageSize){
        Predicate[] predicates = new Predicate[]{
                predicateOptional(qConfirm.seq::lt,prevLastConfirmSeq),
        };

        List<Confirm> confirmList = from(qConfirm)
                .innerJoin(qConfirm.challenge, qChallenge).fetchJoin()
                .innerJoin(qConfirm.user, qUser).fetchJoin()
                .leftJoin(qConfirm.confirmStickerList, qConfirmSticker).fetchJoin()
                .orderBy(qConfirm.seq.desc())
                .where(predicates)
                .limit(pageSize)
                .fetch();

        return confirmList;
    }

    @Override
    public List<Confirm> findAllByChallenge(Challenge challenge, Long prevLastConfirmSeq, Integer pageSize) {
        Predicate[] predicates = new Predicate[]{
                predicateOptional(qConfirm.seq::lt,prevLastConfirmSeq),
                predicateOptional(qConfirm.challenge::eq,challenge),
        };

        List<Confirm> confirmList = from(qConfirm)
                .innerJoin(qConfirm.challenge, qChallenge).fetchJoin()
                .innerJoin(qConfirm.user, qUser).fetchJoin()
                .leftJoin(qConfirm.confirmStickerList, qConfirmSticker).fetchJoin()
                .orderBy(qConfirm.createdAt.desc())
                .where(predicates)
                .limit(pageSize)
                .fetch();

        return confirmList;
    }

    @Override
    public List<Confirm> findByUserByOrderDesc(Long userSeq,Long prevLastConfirmSeq, Integer pageSize){
        List<User> user=from(qUser)
                .where(qUser.seq.eq(userSeq))
                .fetch();

        List<Challenge> challengeList=from(qChallenge)
                .fetch();

        Predicate[] predicates = new Predicate[]{
                predicateOptional(qConfirm.seq::lt,prevLastConfirmSeq),
        };

        List<Confirm> confirmList = from(qConfirm)
                .innerJoin(qConfirm.challenge,qChallenge)
                .innerJoin(qConfirm.user,qUser)
                .where(qConfirm.user.in(user),qConfirm.challenge.in(challengeList))
                .where(predicates)
                .orderBy(qConfirm.createdAt.desc())
                .limit(pageSize)
                .fetch();

        return confirmList;

    }

    @Override
    public Confirm findBySeq(Long seq){

        Confirm confirm = from(qConfirm)
                .where(qConfirm.seq.in(seq))
                .fetchOne();

        return confirm;
    }

    @Transactional
    @Override
    public void updateStateSupportedCnt(Confirm confirm, ConfirmStateType state, Integer cnt){
        update(qConfirm)
                .set(qConfirm.state,state)
                .set(qConfirm.reportedCnt,cnt)
                .where(qConfirm.eq(confirm))
                .execute();

        return;
    }

    @Override
    public Optional<Confirm> findByChallenge(Challenge challenge){
        return from(qConfirm)
                .select(qConfirm)
                .where(qConfirm.challenge.eq(challenge))
                .orderBy(qConfirm.createdAt.asc())
                .fetch()
                .stream()
                .distinct()
                .findFirst();
    }

    @Override
    public List<Confirm> findByCreatedAt(LocalDateTime localDateTime){

        return from(qConfirm)
                .innerJoin(qConfirm.challenge, qChallenge).fetchJoin()
                .innerJoin(qConfirm.user, qUser).fetchJoin()
                .where(qConfirm.createdAt.goe(localDateTime))
                .fetch();
    }

    @Override
    public List<Confirm> findByUserAndPeriod(User user, Challenge challenge, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Predicate[] predicates = new Predicate[]{
                qConfirm.createdAt.goe(startDateTime),
                qConfirm.createdAt.lt(endDateTime),
                qConfirm.user.eq(user),
                qConfirm.challenge.eq(challenge)
        };

        return from(qConfirm)
                .innerJoin(qConfirm.challenge, qChallenge).fetchJoin()
                .innerJoin(qConfirm.user, qUser).fetchJoin()
                .where(predicates)
                .fetch();
    }
    @Override
    public List<Confirm> findByPeriodAndPaging(LocalDateTime startDateTime,Long prevLastConfirmSeq, Integer pageSize){
        Predicate[] predicates = new Predicate[]{
                qConfirm.createdAt.goe(startDateTime),
                predicateOptional(qConfirm.seq::lt,prevLastConfirmSeq)
        };

        return from(qConfirm)
                .leftJoin(qConfirm.challenge,qChallenge).fetchJoin()
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
