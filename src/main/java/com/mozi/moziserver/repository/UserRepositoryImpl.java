package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.entity.QUserReward;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserReward;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositorySupport {
    private final QUser qUser = QUser.user;
    private final QUserReward qUserReward = QUserReward.userReward;

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public List<User> findAllActiveUserByUserReward(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        return from(qUser)
                .innerJoin(qUserReward).on(qUserReward.user.seq.eq(qUser.seq))
                .where(qUserReward.updatedAt.goe(startDateTime).and(qUserReward.updatedAt.lt(endDateTime)))
                .fetch();
    }
}