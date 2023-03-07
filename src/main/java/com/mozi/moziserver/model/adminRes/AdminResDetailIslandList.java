package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.DetailIsland;
import lombok.Getter;

@Getter
class AdminResDetailIslandList {

    private final Long detailIslandSeq;
    private final Integer animalTurn;
    private final Integer itemTurn;
    private final String imgUrl;
    private final String thumbnailImgUrl;

    private AdminResDetailIslandList(DetailIsland detailIsland) {
        this.detailIslandSeq = detailIsland.getSeq();
        this.animalTurn = detailIsland.getAnimalTurn();
        this.itemTurn = detailIsland.getItemTurn();
        this.imgUrl = detailIsland.getImgUrl();
        this.thumbnailImgUrl = detailIsland.getThumbnailImgUrl();
    }

    public static AdminResDetailIslandList of(DetailIsland detailIsland) {
        return new AdminResDetailIslandList(detailIsland);
    }
}
