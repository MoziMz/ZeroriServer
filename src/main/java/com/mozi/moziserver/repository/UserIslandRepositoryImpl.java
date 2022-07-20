package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserIslandRepositoryImpl extends QuerydslRepositorySupport implements UserIslandRepositorySupport {
    private final QUserIsland qUserIsland = QUserIsland.userIsland;
    private final QIslandImg qIslandImg = QIslandImg.islandImg;
    private final QIsland qIsland = QIsland.island;
    private final QUser qUser = QUser.user;

    public UserIslandRepositoryImpl() {
        super(UserIsland.class);
    }

    @Override
    public List<UserIsland> findAllByUserOrderByType(User user) {
        return from(qUserIsland)
                .innerJoin(qUserIsland.user, qUser).fetchJoin()
                .innerJoin(qUserIsland.island, qIsland).fetchJoin()
                .innerJoin(qUserIsland.islandImg, qIslandImg).fetchJoin()
                .where(qUserIsland.user.eq(user))
                .orderBy(qIsland.type.asc())
                .fetch();
    }
}
