package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.AnimalItem;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AdminResAnimal {
    private final Long seq;
    private final Integer turn;
    private final String name;
    private final String imgUrl;
    private final String fullBodyImgUrl;
    private final String postboxAnimalContent;
    private final Long islandSeq;
    private final List<AdminResAnimalItem> animalItemList;

    private AdminResAnimal(Animal animal,List<AnimalItem> animalItemList){
        this.seq=animal.getSeq();
        this.turn= animal.getTurn();
        this.name=animal.getName();
        this.imgUrl=animal.getImgUrl();
        this.fullBodyImgUrl=animal.getFullBodyImgUrl();
        this.postboxAnimalContent=animal.getPostboxAnimalContent();
        this.islandSeq=animal.getIsland().getSeq();
        this.animalItemList=animalItemList.stream().map(animalItem -> AdminResAnimalItem.of(animalItem))
                .collect(Collectors.toList());
    }

    public static AdminResAnimal of(Animal animal,List<AnimalItem> animalItemList) {
        return new AdminResAnimal(animal,animalItemList);
    }
}
