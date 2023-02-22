package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.EmailCheckAuth;
import com.mozi.moziserver.model.entity.QEmailCheckAuth;
import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.entity.User;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class EmailCheckAuthRepositoryImpl extends QuerydslRepositorySupport implements EmailCheckAuthRepositorySupport {
    QEmailCheckAuth qEmailCheckAuth = QEmailCheckAuth.emailCheckAuth;
    QUser qUser = QUser.user;

    public EmailCheckAuthRepositoryImpl() {
        super(EmailCheckAuth.class);
    }

    @Override
    public EmailCheckAuth getUserEmail(User user) {
        return from(qEmailCheckAuth)
                .innerJoin(qEmailCheckAuth.user, qUser)
                .where(qEmailCheckAuth.user.eq(user))
                .orderBy(qEmailCheckAuth.createdAt.desc())
                .fetchFirst();
    }
}
