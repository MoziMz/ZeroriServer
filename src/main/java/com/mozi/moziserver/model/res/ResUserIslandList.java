package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.UserIsland;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ResUserIslandList {
    private Integer type;
    private Integer rewardLevel;
    private Integer maxRewardLevel;
    private String imgUrl;

    private ResUserIslandList(UserIsland userIsland) {
        this.type = userIsland.getType();
        this.rewardLevel = userIsland.getRewardLevel();
        this.maxRewardLevel = userIsland.getIslandType().getMaxRewardLevel();
        this.imgUrl = userIsland.getIslandReward().getImgUrl();
    }

    public static ResUserIslandList of(UserIsland userIsland) {
        return new ResUserIslandList(userIsland);
    }
}
