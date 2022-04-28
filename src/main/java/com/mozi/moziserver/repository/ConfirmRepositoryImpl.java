package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.entity.QChallenge;
import com.mozi.moziserver.model.entity.QConfirm;
import com.mozi.moziserver.model.entity.QUser;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
                .fetch()
                .stream()
                .collect(Collectors.toList());

        List<Challenge> challengeList=from(qChallenge)
                .fetch()
                .stream()
                .collect(Collectors.toList());

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
                .fetch()
                .stream()
                .collect(Collectors.toList());

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
    public void updateDeclarationState(Confirm confirm,Byte state){
        update(qConfirm)
                .set(qConfirm.confirmState,state)
                .where(qConfirm.eq(confirm))
                .execute();

        entityManager.clear();
    }

    @Override
    public Long findSeq() {
        return from(qConfirm)
                .select(qConfirm.seq)
                .orderBy(qConfirm.seq.desc())
                .fetchFirst();
    }


    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}
