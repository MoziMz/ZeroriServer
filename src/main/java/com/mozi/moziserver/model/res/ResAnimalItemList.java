package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.AnimalItem;
import lombok.Getter;

import java.text.BreakIterator;

@Getter
public class ResAnimalItemList {

    private Integer turn;
    private String name;
    private String colorImgUrl;
    private String blackImgUrl;
    private boolean coloredState; // 유저의 아이템 획득 여부
    private Integer acquisitionRequiredPoint;

    private ResAnimalItemList(AnimalItem animalItem) {
        this.turn = animalItem.getTurn();
        this.name = animalItem.getName();
        this.colorImgUrl = animalItem.getColorImgUrl();
        this.blackImgUrl = animalItem.getBlackImgUrl();
        this.coloredState = animalItem.isAcquisition();
        this.acquisitionRequiredPoint = animalItem.getAcquisitionRequiredPoint();
    }

    public static ResAnimalItemList of(AnimalItem animalItem) {
        return new ResAnimalItemList(animalItem);
    }
}
