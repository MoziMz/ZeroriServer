package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.AnimalItem;
import lombok.Getter;

@Getter
public class ResPreparationItemList {
    private Integer turn;
    private String name;
    private String colorImgUrl;
    private String blackImgUrl;
    private boolean coloredState;

    private ResPreparationItemList(AnimalItem preparationItem, Integer level) {
        this.turn = preparationItem.getTurn();
        this.name = preparationItem.getName();
        this.colorImgUrl = preparationItem.getColorImgUrl();
        this.blackImgUrl = preparationItem.getBlackImgUrl();
        this.coloredState = turn < level;
    }

    public static ResPreparationItemList of(AnimalItem preparationItem, Integer level) {
        return new ResPreparationItemList(preparationItem, level);
    }
}
