package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.Animal;
import lombok.Getter;


@Getter
public class AdminResAnimalList {

    private final Long seq;
    private final Integer turn;
    private final String name;
    private final String imgUrl;
    private final String fullBodyImgUrl;
    private final String postboxAnimalContent;
    private final Long islandSeq;

    private AdminResAnimalList(Animal animal){
        this.seq=animal.getSeq();
        this.turn= animal.getTurn();
        this.name=animal.getName();
        this.imgUrl=animal.getImgUrl();
        this.fullBodyImgUrl=animal.getFullBodyImgUrl();
        this.postboxAnimalContent=animal.getPostboxAnimalContent();
        this.islandSeq=animal.getIsland().getSeq();
    }

    public static AdminResAnimalList of(Animal animal) {
        return new AdminResAnimalList(animal);
    }
}
