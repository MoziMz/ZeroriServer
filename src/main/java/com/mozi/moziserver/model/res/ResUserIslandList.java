package com.mozi.moziserver.model.res;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.DetailIsland;
import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.model.entity.UserIsland;
import lombok.Getter;

@Getter
public class ResUserIslandList {
    private Long type;  // TODO ERASE (NOT USED V2)
    private Integer totalRequiredPoint;  // TODO ERASE (NOT USED V2)
    private Long islandSeq;
    private final String name;
    private final String description;

    private Integer openRequiredPoint;
    private final Integer currentRequiredPoint;
    private final String imgUrl;
    private final String thumbnailImgUrl;
    private final boolean existenceState;
    private final boolean openableState;
    private final Integer animalLevel;

    // TODO ERASE (NOT USED V2)
    private ResUserIslandList(Island island, UserIsland userIsland, UserIsland lastUserIsland, int currentUserPoint) {
        this.type = island.getSeq();
        this.name = island.getName();
        this.description = island.getDescription() + " 동물들을 도울 수 있어요";
        this.totalRequiredPoint = island.getOpenRequiredPoint();
        this.currentRequiredPoint = island.getSeq() <= lastUserIsland.getDetailIsland().getIsland().getSeq()
                ? 0 : island.getSeq() == lastUserIsland.getDetailIsland().getIsland().getSeq() + 1
                ? Math.max(Constant.islandMaxPoint - currentUserPoint, 0) : Constant.islandMaxPoint;
        this.imgUrl = userIsland != null ? userIsland.getDetailIsland().getImgUrl() : null;
        this.thumbnailImgUrl = userIsland != null ? userIsland.getDetailIsland().getThumbnailImgUrl() : null;
        this.existenceState = userIsland != null;
        this.openableState = island.getSeq() == lastUserIsland.getSeq() + 1 && currentUserPoint >= Constant.islandMaxPoint;
        this.animalLevel = userIsland != null ? userIsland.getDetailIsland().getAnimalTurn() : 0;
    }

    private ResUserIslandList(Island island, DetailIsland detailIsland, Long userLastIslandSeq, int userPoint) {
        this.islandSeq = island.getSeq();
        this.name = island.getName();
        this.description = island.getDescription() + " 동물들을 도울 수 있어요";
        this.openRequiredPoint = island.getOpenRequiredPoint();
        this.currentRequiredPoint = island.getSeq() <= userLastIslandSeq
                ? 0 : island.getSeq() == userLastIslandSeq + 1
                ? Math.max(island.getOpenRequiredPoint() - userPoint, 0) : island.getOpenRequiredPoint();
        this.imgUrl = detailIsland != null ? detailIsland.getImgUrl() : null;
        this.thumbnailImgUrl = detailIsland != null ? detailIsland.getThumbnailImgUrl() : null;
        this.existenceState = detailIsland != null;
        this.openableState = island.getSeq() == userLastIslandSeq + 1 && userPoint >= island.getOpenRequiredPoint();
        this.animalLevel = detailIsland != null ? detailIsland.getAnimalTurn() : 0;
    }

    // TODO ERASE (NOT USED V2)
    public static ResUserIslandList of(Island island, UserIsland userIsland, UserIsland lastUserIsland, int currentUserPoint) {
        return new ResUserIslandList(island, userIsland, lastUserIsland, currentUserPoint);
    }

    public static ResUserIslandList of(Island island, DetailIsland detailIsland, Long userLastIslandSeq, int userPoint) {
        return new ResUserIslandList(island, detailIsland, userLastIslandSeq, userPoint);
    }
}
