package com.mozi.moziserver.model.res;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.model.entity.UserIsland;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ResUserIslandList {
    private Integer type;
    private String name;
    private String description;
    private Integer currentRequiredPoint;
    private String imgUrl = null;
    private boolean existenceState = false;

    private ResUserIslandList(Island island, UserIsland userIsland, int finalUserIsland, int currentUserPoint) {
        this.type = island.getType();
        this.name = island.getName();
        this.description = island.getDescription();
        this.currentRequiredPoint = 0;
        if (island.getType() >= finalUserIsland) {
            if (island.getType() == finalUserIsland) {
                currentRequiredPoint = Constant.islandMaxPoionts - currentUserPoint < 0 ? 0 : Constant.islandMaxPoionts - currentUserPoint;
            }
            else
                currentRequiredPoint = Constant.islandMaxPoionts;
        }
        if (userIsland != null) {
            this.existenceState = true;
            this.imgUrl = userIsland.getIslandImg().getImgUrl();
        }
    }

    public static ResUserIslandList of(Island island, UserIsland userIsland, int finalUserIsland, int currentUserPoint) {
        return new ResUserIslandList(island, userIsland, finalUserIsland, currentUserPoint);
    }
}
