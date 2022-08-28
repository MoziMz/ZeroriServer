package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.entity.QChallenge;
import com.mozi.moziserver.model.entity.QConfirm;
import com.mozi.moziserver.model.entity.QUser;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

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
                .orderBy(qConfirm.createdAt.desc())
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

    @Override
    public void updateDeclarationCnt(Confirm confirm,Byte state,Integer cnt){
        update(qConfirm)
                .set(qConfirm.declarationState,state)
                .set(qConfirm.declarationCnt,cnt)
                .where(qConfirm.eq(confirm))
                .execute();

        return;
    }

    @Override
    public Long findSeq() {
        return from(qConfirm)
                .select(qConfirm.seq)
                .orderBy(qConfirm.seq.desc())
                .fetchFirst();
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
                .where(qConfirm.createdAt.before(localDateTime)
                        .and(qConfirm.createdAt.after(localDateTime.minus(7, ChronoUnit.DAYS))))
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

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}
