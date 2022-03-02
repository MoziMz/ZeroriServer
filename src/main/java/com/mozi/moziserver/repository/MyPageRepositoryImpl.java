package com.mozi.moziserver.repository;

import com.mozi.moziserver.common.UserState;
import com.mozi.moziserver.model.entity.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

public class MyPageRepositoryImpl extends QuerydslRepositorySupport implements MyPageRepositorySupport {
    private final QUser qUser = QUser.user;
    private final QUserAuth qUserAuth = QUserAuth.userAuth;
    private final QEmailAuth qEmailAuth = QEmailAuth.emailAuth;
    @PersistenceContext
    private EntityManager entityManager;

    public MyPageRepositoryImpl() {
        super(UserAuth.class);
    }

    @Override
    public UserAuth getUserInfo(User user) {
        return from(qUserAuth)
                .innerJoin(qUserAuth.user, qUser).fetchJoin()
                .where(qUserAuth.user.eq(user))
                .fetchOne();
    }

    @Override
    public UserAuth getAllNickName(
            User user,
            String nickName
    ) {
        return from(qUserAuth)
                .innerJoin(qUser).on(qUserAuth.user.seq.eq(qUser.seq))
                .where(qUser.state.eq(UserState.ACTIVE)
                        .and(qUser.nickName.eq(nickName)))
                .fetchOne();
    }

    @Override
    public UserAuth getAllEmail(User user, String email) {
        return from(qUserAuth)
                .join(qUser).on(qUserAuth.user.seq.eq(qUser.seq))
                .where(qUser.state.eq(UserState.ACTIVE))
                .where(qUser.email.eq(email).or(qUserAuth.id.eq(email)))
                .fetchOne();
    }

    @Override
    public void updateUserEmail(User user, String email) {
         update(qUser)
                .set(qUser.email, email)
                .where(qUser.eq(user))
                .execute();

         entityManager.clear();
    }

    @Override
    public void updateUserNickName(User user, String nickName) {
        update(qUser)
                .set(qUser.nickName, nickName)
                .where(qUser.eq(user))
                .execute();

        entityManager.clear();
    }

    @Override
    public void updateUserPassword(User user, String pw) {

        update(qUserAuth)
                .set(qUserAuth.pw, pw)
                .where(qUser.eq(user))
                .execute();

        update(qEmailAuth)
                .set(qEmailAuth.pw, pw)
                .where(qUser.eq(user))
                .execute();

        entityManager.clear();
    }
}
