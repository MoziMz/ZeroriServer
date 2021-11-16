package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserIslandRepositoryImpl extends QuerydslRepositorySupport implements UserIslandRepositorySupport {
    private final QUserIsland qUserIsland = QUserIsland.userIsland;
    private final QIslandReward qIslandReward = QIslandReward.islandReward;
    private final QIslandType qIslandType = QIslandType.islandType;

    public UserIslandRepositoryImpl() {
        super(UserIsland.class);
    }

    @Override
    public List<UserIsland> findAllByUser(User user) {
        return from(qUserIsland)
                .innerJoin(qUserIsland.islandType, qIslandType).fetchJoin()
                .innerJoin(qUserIsland.islandReward, qIslandReward).fetchJoin()
                .fetch();

//        return from(qPost)
//                .select(new QPostProjection(qPost, qPostLike, qPostBookmark))
//                .innerJoin(qPost.user, qUser).fetchJoin()
//                .leftJoin(qPost.pictures, qPostPicture).fetchJoin()
//                .leftJoin(qPostLike)
//                .on(qPostLike.postSeq.eq(qPost.seq)
//                        .and(qPostLike.userSeq.eq(userSeq)))
//                .leftJoin(qPostBookmark)
//                .on(qPostBookmark.postSeq.eq(qPost.seq)
//                        .and(qPostBookmark.userSeq.eq(userSeq)))
//                .where(qPost.seq.eq(seq))
//                .fetch()
//                .stream()
//                .distinct()
//                .map(PostProjection::getPost)
//                .findFirst();
    }
}
