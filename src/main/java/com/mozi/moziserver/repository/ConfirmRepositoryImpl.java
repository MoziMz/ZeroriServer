package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.entity.QChallenge;
import com.mozi.moziserver.model.entity.QConfirm;
import com.mozi.moziserver.model.entity.QConfirmId;
import com.mozi.moziserver.model.entity.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

public class ConfirmRepositoryImpl extends QuerydslRepositorySupport implements ConfirmRepositorySupport{
    private final QConfirm qConfirm = QConfirm.confirm;
    private final QConfirmId qConfirmId=QConfirmId.confirmId;
    private final QUser qUser= QUser.user;
    private final QChallenge qChallenge=QChallenge.challenge;
    @PersistenceContext
    private EntityManager entityManager;


    public ConfirmRepositoryImpl() {super(Confirm.class);}

    @Override
    public List<Confirm> findAllByOrderDesc() {

        List<User> userList=from(qUser)
                .fetch()
                .stream()
                .collect(Collectors.toList());

        List<Challenge> challengeList=from(qChallenge)
                .fetch()
                .stream()
                .collect(Collectors.toList());

        List<Confirm> confirmList = from(qConfirm)
                .innerJoin(qConfirm.id.challenge,qChallenge)
                .innerJoin(qConfirm.id.user,qUser)
                .where(qConfirm.id.user.in(userList),qConfirm.id.challenge.in(challengeList))
                .orderBy(qConfirm.createdAt.desc())
                .fetch()
                .stream()
                .collect(Collectors.toList());

        return confirmList;

    }

    @Override
    public List<Confirm> findByChallengeByOrderDesc(Long seq){
        List<User> userList=from(qUser)
                .fetch()
                .stream()
                .collect(Collectors.toList());

        List<Challenge> challenge=from(qChallenge)
                .where(qChallenge.seq.eq(seq))
                .fetch()
                .stream()
                .collect(Collectors.toList());

        List<Confirm> confirmList = from(qConfirm)
                .innerJoin(qConfirm.id.challenge,qChallenge)
                .innerJoin(qConfirm.id.user,qUser)
                .where(qConfirm.id.user.in(userList),qConfirm.id.challenge.in(challenge))
                .orderBy(qConfirm.createdAt.desc())
                .fetch()
                .stream()
                .collect(Collectors.toList());

        return confirmList;
    }

    @Override
    public List<Confirm> findByUserByOrderDesc(Long userSeq){
        List<User> user=from(qUser)
                .where(qUser.seq.eq(userSeq))
                .fetch()
                .stream()
                .collect(Collectors.toList());

        List<Challenge> challengeList=from(qChallenge)
                .fetch()
                .stream()
                .collect(Collectors.toList());

        List<Confirm> confirmList = from(qConfirm)
                .innerJoin(qConfirm.id.challenge,qChallenge)
                .innerJoin(qConfirm.id.user,qUser)
                .where(qConfirm.id.user.in(user),qConfirm.id.challenge.in(challengeList))
                .orderBy(qConfirm.createdAt.desc())
                .fetch()
                .stream()
                .collect(Collectors.toList());

        return confirmList;

    }

    @Override
    public Confirm findByUserAndChallengeSeqAndDate(Long userSeq,Long seq,Date date){
        List<User> user=from(qUser)
                .where(qUser.seq.eq(userSeq))
                .fetch()
                .stream()
                .collect(Collectors.toList());

        List<Challenge> challenge=from(qChallenge)
                .where(qChallenge.seq.eq(seq))
                .fetch()
                .stream()
                .collect(Collectors.toList());

        Confirm confirm = from(qConfirm)
                .innerJoin(qConfirm.id.challenge,qChallenge)
                .innerJoin(qConfirm.id.user,qUser)
                .where(qConfirm.id.user.in(user),qConfirm.id.challenge.in(challenge),qConfirm.id.date.in(date))
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

}
