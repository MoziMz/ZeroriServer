package com.mozi.moziserver.repository;

import com.mozi.moziserver.common.UserState;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;

public class UserAuthRepositoryImpl extends QuerydslRepositorySupport implements UserAuthRepositorySupport {
    private final QUserAuth qUserAuth = QUserAuth.userAuth;
    private final QUser qUser = QUser.user;
    private final QUserReward qUserReward = QUserReward.userReward;

    EntityManager entityManager;

    public UserAuthRepositoryImpl() {
        super(UserAuth.class);
    }

    @Override
    public User findUserSeqByEmail(String email) {
        return from(qUserAuth)
                .select(qUserAuth.user)
                .where(qUserAuth.id.eq(email))
                .fetchOne();
    }

    @Override
    public UserAuth findByUser(User user) {
        return from(qUserAuth)
                .where(qUserAuth.user.eq(user))
                .fetchOne();
    }

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    @Override
    public List<UserAuth> findAllByKeywordAndTypeAndState(
            String keyword,
            UserAuthType userAuthType,
            UserState userState,
            Integer pageNumber,
            Integer pageSize
    ) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qUserAuth.type::eq, userAuthType),
                predicateOptional(qUser.state::eq, userState)
        };

        return from(qUserAuth)
                .innerJoin(qUserAuth.user, qUser).fetchJoin()
                .where(predicates)
                .where(StringUtils.hasLength(keyword) ? qUser.nickName.like('%' + keyword + '%').or(qUser.email.like('%' + keyword + '%')) : null)
                .orderBy(qUserAuth.user.seq.asc())
                .limit(pageSize)
                .offset(pageNumber * pageSize)
                .fetch();
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}
