package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
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
    public User getUserNickName(
            User user,
            String nickName
    ) {
        return from(qUser)
                .where(qUser.nickName.eq(nickName))
                .fetchOne();
    }

    @Override
    public User getUserEmail(User user, String email) {
        return from(qUser)
                .where(qUser.email.eq(email))
                .fetchOne();
    }

    @Override
    @Transactional
    public void updateUserEmail(User user, String email) {
         update(qUser)
                .set(qUser.email, email)
                .where(qUser.eq(user))
                .execute();

         entityManager.clear();

//        JPAQueryFactory jpaQueryFactory = null;
//
//        long execute = jpaQueryFactory
//                .update(qUser)
//                .set(qUser.email, email)
//                .where(qUser.seq.eq(seq))
//                .execute();

    }

    @Override
    @Transactional
    public void updateUserInfo(User user, String nickName, String pw) {
        update(qUser)
                .set(qUser.nickName, nickName)
                .where(qUser.eq(user))
                .execute();

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
