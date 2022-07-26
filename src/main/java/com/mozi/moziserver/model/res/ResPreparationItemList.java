package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.PreparationItem;
import lombok.Getter;

@Getter
public class ResPreparationItemList {
    private Integer turn;
    private String name;
    private String colorImgUrl;
    private String blackImgUrl;
    private boolean coloredState;

    private ResPreparationItemList(PreparationItem preparationItem, Integer level) {
        this.turn = preparationItem.getTurn();
        this.name = preparationItem.getName();
        this.colorImgUrl = preparationItem.getColorImgUrl();
        this.blackImgUrl = preparationItem.getBlackImgUrl();
        this.coloredState = turn < level;
    }

    public static ResPreparationItemList of(PreparationItem preparationItem, Integer level) {
        return new ResPreparationItemList(preparationItem, level);
    }
}
