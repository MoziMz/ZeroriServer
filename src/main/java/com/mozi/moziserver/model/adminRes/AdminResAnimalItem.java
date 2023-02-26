package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.AnimalItem;
import lombok.Getter;

import java.util.List;

@Getter
public class AdminResAnimalItem {

    private final Long seq;
    private final Integer turn;
    private final String name;
    private final Integer acquisitionRequiredPoint;
    private final String colorImgUrl;
    private final String blackImgUrl;
    private final Long animalSeq;

    private AdminResAnimalItem(AnimalItem animalItem){
        this.seq=animalItem.getSeq();
        this.turn= animalItem.getTurn();
        this.name=animalItem.getName();
        this.acquisitionRequiredPoint=animalItem.getAcquisitionRequiredPoint();
        this.colorImgUrl= animalItem.getColorImgUrl();
        this.blackImgUrl= animalItem.getBlackImgUrl();
        this.animalSeq = animalItem.getAnimal().getSeq();
    }

    public static AdminResAnimalItem of(AnimalItem animalItem) {
        return new AdminResAnimalItem(animalItem);
    }
}
