package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserIslandRepositoryImpl extends QuerydslRepositorySupport implements UserIslandRepositorySupport {
    private final QUserIsland qUserIsland = QUserIsland.userIsland;
    private final QDetailIsland qDetailIsland = QDetailIsland.detailIsland;
    private final QIsland qIsland = QIsland.island;
    private final QUser qUser = QUser.user;

    public UserIslandRepositoryImpl() {
        super(UserIsland.class);
    }

    @Override
    public UserIsland findTopByUser(User user) {

        return from(qUserIsland)
                .innerJoin(qUserIsland.detailIsland, qDetailIsland).fetchJoin()
                .orderBy(qDetailIsland.island.seq.desc())
                .fetchFirst();
    }

    @Override
    public List<UserIsland> findAllByUserOrderByIsland(User user) {

        return from(qUserIsland)
                .innerJoin(qUserIsland.user, qUser).fetchJoin()
                .innerJoin(qUserIsland.detailIsland, qDetailIsland).fetchJoin()
                .innerJoin(qDetailIsland.island, qIsland).fetchJoin()
                .where(qUserIsland.user.eq(user))
                .orderBy(qDetailIsland.island.seq.asc())
                .fetch();
    }

    @Override
    public UserIsland findTopByUserOrderByIsland(User user) {

        return from(qUserIsland)
                .innerJoin(qUserIsland.user, qUser).fetchJoin()
                .innerJoin(qUserIsland.detailIsland, qDetailIsland).fetchJoin()
                .innerJoin(qDetailIsland.island, qIsland).fetchJoin()
                .where(qUserIsland.user.eq(user))
                .orderBy(qDetailIsland.island.seq.asc())
                .fetchOne();
    }

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    @Override
    public List<UserIsland> findAllByUserIn(List<User> userList) {
        return from(qUserIsland)
                .leftJoin(qUserIsland.user, qUser).fetchJoin()
                .where(qUser.in(userList))
                .fetch();
    }

    @Override
    public boolean existsByDetailIsland(DetailIsland detailIsland) {
        return from(qUserIsland)
                .where(qUserIsland.detailIsland.eq(detailIsland))
                .select(qUserIsland.seq)
                .fetchFirst() != null;
    }
}
