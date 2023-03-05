package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.DetailIsland;
import com.mozi.moziserver.model.entity.Island;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AdminResIsland {
    private final Long islandSeq;
    private final String name;
    private final String description;
    private final Integer openRequiredPoint;
    private final List<AdminResDetailIsland> islandImgList;

    private AdminResIsland(Island island, List<DetailIsland> detailIslandList) {
        this.islandSeq = island.getSeq();
        this.name = island.getName();
        this.description = island.getDescription();
        this.openRequiredPoint = island.getOpenRequiredPoint();
        this.islandImgList = detailIslandList.stream()
                .sorted(Comparator.comparing(DetailIsland::getAnimalTurn))
                .map(AdminResDetailIsland::of)
                .collect(Collectors.toList());
    }

    public static AdminResIsland of(Island island, List<DetailIsland> detailIslandList) {
        return new AdminResIsland(island, detailIslandList);
    }
}

@Getter
class AdminResDetailIsland {

    private final Long detailIslandSeq;
    private final Integer animalTurn;
    private final Integer itemTurn;
    private final String imgUrl;
    private final String thumbnailImgUrl;

    private AdminResDetailIsland(DetailIsland detailIsland) {
        this.detailIslandSeq = detailIsland.getSeq();
        this.animalTurn = detailIsland.getAnimalTurn();
        this.itemTurn = detailIsland.getItemTurn();
        this.imgUrl = detailIsland.getImgUrl();
        this.thumbnailImgUrl = detailIsland.getThumbnailImgUrl();
    }

    public static AdminResDetailIsland of(DetailIsland detailIsland) {
        return new AdminResDetailIsland(detailIsland);
    }
}