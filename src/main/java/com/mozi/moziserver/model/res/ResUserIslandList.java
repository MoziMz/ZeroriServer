package com.mozi.moziserver.model.res;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.model.entity.UserIsland;
import lombok.Getter;

@Getter
public class ResUserIslandList {
    private final Integer type;
    private final String name;
    private final String description;
    private final Integer totalRequiredPoint;
    private final Integer currentRequiredPoint;
    private final String imgUrl;
    private final String thumbnailImgUrl;
    private final boolean existenceState;
    private final boolean openableState;
    private final Integer animalLevel;

    private ResUserIslandList(Island island, UserIsland userIsland, UserIsland lastUserIsland, int currentUserPoint) {
        this.type = island.getType();
        this.name = island.getName();
        this.description = island.getDescription() + " 동물들을 도울 수 있어요";
        this.totalRequiredPoint = island.getMaxPoint();
        this.currentRequiredPoint = island.getType() <= lastUserIsland.getType()
                ? 0 : island.getType() == lastUserIsland.getType() + 1
                ? Math.max(Constant.islandMaxPoint - currentUserPoint, 0) : Constant.islandMaxPoint;
        this.imgUrl = userIsland != null ? userIsland.getIslandImg().getImgUrl() : null;
        this.thumbnailImgUrl = userIsland != null ? userIsland.getIslandImg().getThumbnailImgUrl() : null;
        this.existenceState = userIsland != null;
        this.openableState = island.getType() == lastUserIsland.getType() + 1 && currentUserPoint >= Constant.islandMaxPoint;
        this.animalLevel = userIsland != null ? userIsland.getRewardLevel() - 1 : 0;
    }

    public static ResUserIslandList of(Island island, UserIsland userIsland, UserIsland lastUserIsland, int currentUserPoint) {
        return new ResUserIslandList(island, userIsland, lastUserIsland, currentUserPoint);
    }
}
