package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.PreparationItem;
import com.mozi.moziserver.model.mappedenum.AnimalType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResPostboxMessageAnimal {
    private String animalName;
    private String content;
    private String imgUrl;
    private List<ResPreparationItemList> preparationItemList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ResPostboxMessageAnimal(PostboxMessageAnimal postboxMessageAnimal, List<PreparationItem> preparationItemList) {
        this.animalName = postboxMessageAnimal.getAnimal().getName();
        this.content = postboxMessageAnimal.getContent();
        this.imgUrl = postboxMessageAnimal.getAnimal().getImgUrl();
        this.preparationItemList = preparationItemList.stream().map(e -> ResPreparationItemList.of(e, postboxMessageAnimal.getLevel())).collect(Collectors.toList());
        this.createdAt = postboxMessageAnimal.getCreatedAt();
        this.updatedAt = postboxMessageAnimal.getUpdatedAt();
    }

    public static ResPostboxMessageAnimal of(PostboxMessageAnimal postboxMessageAnimal, List<PreparationItem> preparationItemList){
        return new ResPostboxMessageAnimal(postboxMessageAnimal, preparationItemList);
    }
}
