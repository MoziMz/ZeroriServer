package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.AnimalItem;
import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResPostboxMessageAnimal {
    private Long animalSeq;
    private String animalName;
    private String content;
    private String imgUrl;
    private List<ResPreparationItemList> preparationItemList; // TODO ERASE V2
    private List<ResAnimalItemList> animalItemList;
    private Integer thisWeekUserRewardPoint;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // TODO ERASE V2
    private ResPostboxMessageAnimal(PostboxMessageAnimal postboxMessageAnimal, List<AnimalItem> preparationItemList) {
        this.animalSeq = postboxMessageAnimal.getAnimal().getSeq();
        this.animalName = postboxMessageAnimal.getAnimal().getName();
        this.content = postboxMessageAnimal.getAnimal().getPostboxAnimalContent();
        this.imgUrl = postboxMessageAnimal.getAnimal().getImgUrl();
        this.preparationItemList = preparationItemList.stream().map(e -> ResPreparationItemList.of(e, postboxMessageAnimal.getLevel())).collect(Collectors.toList());
        this.createdAt = postboxMessageAnimal.getCreatedAt();
        this.updatedAt = postboxMessageAnimal.getUpdatedAt();
    }

    // V2
    private ResPostboxMessageAnimal(PostboxMessageAnimal postboxMessageAnimal, Integer thisWeekUserRewardPoint) {
        this.animalSeq = postboxMessageAnimal.getAnimal().getSeq();
        this.animalName = postboxMessageAnimal.getAnimal().getName();
        this.content = postboxMessageAnimal.getAnimal().getPostboxAnimalContent();
        this.imgUrl = postboxMessageAnimal.getAnimal().getImgUrl();
        this.animalItemList = postboxMessageAnimal.getAnimalItemList().stream()
                .map(itemList -> ResAnimalItemList.of(itemList)).collect(Collectors.toList());
        this.thisWeekUserRewardPoint = thisWeekUserRewardPoint;
        this.createdAt = postboxMessageAnimal.getCreatedAt();
        this.updatedAt = postboxMessageAnimal.getUpdatedAt();
    }

    // TODO ERASE V2
    public static ResPostboxMessageAnimal of(PostboxMessageAnimal postboxMessageAnimal, List<AnimalItem> preparationItemList){
        return new ResPostboxMessageAnimal(postboxMessageAnimal, preparationItemList);
    }

    // V2
    public static ResPostboxMessageAnimal of(PostboxMessageAnimal postboxMessageAnimal, Integer thisWeekUserRewardPoint) {
        return new ResPostboxMessageAnimal(postboxMessageAnimal, thisWeekUserRewardPoint);
    }
}
