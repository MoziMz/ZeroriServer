package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserIsland;
import com.mozi.moziserver.model.entity.UserReward;
import lombok.Getter;

@Getter
public class AdminResUserRewardList {

    private final AdminResUser userInfo;
    private final Integer point;
    private final Integer lastIslandType;
    private final Integer lastIslandLevel;

    private AdminResUserRewardList(User user, UserReward userReward, UserIsland userIsland) {
        this.userInfo = AdminResUser.of(user);
        this.point = userReward.getPoint();
        this.lastIslandType = userIsland.getType();
        this.lastIslandLevel = userIsland.getRewardLevel();
    }

    public static AdminResUserRewardList of(User user, UserReward userReward, UserIsland userIsland) {
        return new AdminResUserRewardList(user, userReward, userIsland);
    }
}
