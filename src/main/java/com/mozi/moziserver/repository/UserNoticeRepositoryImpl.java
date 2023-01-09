package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.UserNoticeType;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

public class UserNoticeRepositoryImpl extends QuerydslRepositorySupport implements UserNoticeRepositorySupport{
    private final QUserNotice qUserNotice = QUserNotice.userNotice;

    private final QUser qUser = QUser.user;

    public UserNoticeRepositoryImpl(){super(UserNotice.class);}

    @Override
    public Optional<UserNotice> findOneByUserAndTypeAndCheckedState(User user, UserNoticeType type) {

        final Predicate[] predicates = new Predicate[]{
                qUserNotice.user.eq(user),
                qUserNotice.checkedState.eq(false),
                qUserNotice.type.eq(type)
        };

        return Optional.ofNullable(from(qUserNotice)
                .innerJoin(qUserNotice.user,qUser).fetchJoin()
                .where(predicates)
                .fetchFirst());
    }
}
