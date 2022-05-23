package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.PreparationItem;
import com.mozi.moziserver.model.mappedenum.AnimalType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResPostboxMessageAnimal {
    private Integer level;
    private String animalName;
    private String content;
    private String imgUrl;
    private List<ResPreparationItemList> preparationItemList;

    private ResPostboxMessageAnimal(PostboxMessageAnimal postboxMessageAnimal, List<PreparationItem> preparationItemList) {
        this.level = postboxMessageAnimal.getLevel();
        this.animalName = postboxMessageAnimal.getAnimal().getAnimalName().getName();
        this.content = postboxMessageAnimal.getAnimal().getContent();
        this.imgUrl = postboxMessageAnimal.getAnimal().getImgUrl();
        this.preparationItemList = preparationItemList.stream().map(ResPreparationItemList::of).collect(Collectors.toList());
    }

    public static ResPostboxMessageAnimal of(PostboxMessageAnimal postboxMessageAnimal, List<PreparationItem> preparationItemList){
        return new ResPostboxMessageAnimal(postboxMessageAnimal, preparationItemList);
    }
}
