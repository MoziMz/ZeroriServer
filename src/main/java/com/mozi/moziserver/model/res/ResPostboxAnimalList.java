package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.PreparationItem;
import com.mozi.moziserver.model.mappedenum.AnimalType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResPostboxAnimalList {
    //    private Long animalSeq;
    private Integer level;
    private AnimalType animalName;
    private String content;
    private String imgUrl;
    private List<ResPreparationItemList> preparationItemList;

    private ResPostboxAnimalList(PostboxMessageAnimal postboxMessageAnimal, List<PreparationItem> preparationItemList) {
//        this.animalSeq = postboxMessageAnimal.getAnimalSeq();
        this.level = postboxMessageAnimal.getLevel();
        this.animalName = postboxMessageAnimal.getAnimal().getAnimalName();
        this.content = postboxMessageAnimal.getAnimal().getContent();
        this.imgUrl = postboxMessageAnimal.getAnimal().getImgUrl();
        this.preparationItemList = preparationItemList.stream().map(ResPreparationItemList::of).collect(Collectors.toList());

    }

    public static ResPostboxAnimalList of(PostboxMessageAnimal postboxMessageAnimal, List<PreparationItem> preparationItemList){
        return new ResPostboxAnimalList(postboxMessageAnimal, preparationItemList);
    }
}
