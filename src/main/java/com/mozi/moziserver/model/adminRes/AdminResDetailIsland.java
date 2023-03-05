package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.DetailIsland;
import lombok.Getter;

@Getter
public class AdminResDetailIsland {
    private final Long detailIslandSeq;
    private final Integer animalTurn;
    private final Integer itemTurn;

    private AdminResDetailIsland(DetailIsland detailIsland) {
        this.detailIslandSeq = detailIsland.getSeq();
        this.animalTurn = detailIsland.getAnimalTurn();
        this.itemTurn = detailIsland.getItemTurn();
    }

    public static AdminResDetailIsland of(DetailIsland detailIsland) {
        return new AdminResDetailIsland(detailIsland);
    }
}
