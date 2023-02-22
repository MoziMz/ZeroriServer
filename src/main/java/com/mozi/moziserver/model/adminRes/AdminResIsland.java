//package com.mozi.moziserver.model.adminRes;
//
//import com.mozi.moziserver.model.entity.Island;
//import com.mozi.moziserver.model.entity.IslandImg;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import lombok.Getter;
//
//@Getter
//public class AdminResIsland {
//
//    private final Integer type;
//    private final String name;
//    private final Integer maxPoint;
//    private final Integer maxRewardLevel;
//    private final String description;
//    private final List<AdminResIslandImg> islandImgList;
//
//    private AdminResIsland(Island island, List<IslandImg> islandImgList) {
//        this.type = island.getType();
//        this.name = island.getName();
//        this.maxPoint = island.getMaxPoint();
//        this.maxRewardLevel = island.getMaxRewardLevel();
//        this.description = island.getDescription();
//        this.islandImgList = islandImgList.stream()
//                .sorted(Comparator.comparing(IslandImg::getLevel))
//                .map(AdminResIslandImg::of)
//                .collect(Collectors.toList());
//    }
//
//    public static AdminResIsland of(Island island, List<IslandImg> islandImgList) {
//        return new AdminResIsland(island, islandImgList);
//    }
//}
//
//@Getter
//class AdminResIslandImg {
//
////    private final Integer type;
//    private final Integer level;
//    private final String imgUrl;
//    private final String thumbnailImgUrl;
//
//    private AdminResIslandImg(IslandImg islandImg) {
//        this.level = islandImg.getLevel();
//        this.imgUrl = islandImg.getImgUrl();
//        this.thumbnailImgUrl = islandImg.getThumbnailImgUrl();
//    }
//
//    public static AdminResIslandImg of(IslandImg islandImg) {
//        return new AdminResIslandImg(islandImg);
//    }
//}