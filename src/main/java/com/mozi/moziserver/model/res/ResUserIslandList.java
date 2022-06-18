package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.model.entity.UserIsland;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ResUserIslandList {
    private Integer type;
    private String name;
    private String description;
    private Integer maxPoint;
    private String imgUrl = null;
    private boolean existence_state = false;

    private ResUserIslandList(Island island, UserIsland userIsland) {
        this.type = island.getType();
        this.name = island.getName();
        this.description = island.getDescription();
        this.maxPoint = island.getMaxPoint();
        if (userIsland != null) {
            this.existence_state = true;
            this.imgUrl = userIsland.getIslandImg().getImgUrl();
        }

    }

    public static ResUserIslandList of(Island island, UserIsland userIsland) {
        return new ResUserIslandList(island, userIsland);
    }
}
