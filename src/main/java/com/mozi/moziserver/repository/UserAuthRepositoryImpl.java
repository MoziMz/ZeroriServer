package com.mozi.moziserver.repository;

import com.mozi.moziserver.common.UserState;
import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.entity.QUserAuth;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;

public class UserAuthRepositoryImpl extends QuerydslRepositorySupport implements UserAuthRepositorySupport {
    private final QUserAuth qUserAuth = QUserAuth.userAuth;
    private final QUser qUser = QUser.user;
    EntityManager entityManager;

    public UserAuthRepositoryImpl() {
        super(UserAuth.class);
    }

    @Override
    public User findUserSeqByEmail(String email){
        return from(qUserAuth)
                .select(qUserAuth.user)
                .where(qUserAuth.id.eq(email))
                .fetchOne();
    }

    @Override
    public UserAuth findByUser(User user){
        return from(qUserAuth)
                .where(qUserAuth.user.eq(user))
                .fetchOne();
    }
}
