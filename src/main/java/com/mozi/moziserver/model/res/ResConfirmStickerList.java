package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.ConfirmSticker;
import com.mozi.moziserver.model.entity.Sticker;
import lombok.Getter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
public class ResConfirmStickerList {

    private String imgUrl;

    private BigDecimal locationX;

    private BigDecimal locationY;

    private BigDecimal angle;

    private BigDecimal inch;

    private ResConfirmStickerList(ConfirmSticker confirmSticker) {

        this.imgUrl=confirmSticker.getSticker().getImgUrl();
        this.locationX=confirmSticker.getLocationX();
        this.locationY=confirmSticker.getLocationY();
        this.angle=confirmSticker.getAngle();
        this.inch=confirmSticker.getInch();

    }

    public static ResConfirmStickerList of(ConfirmSticker confirmSticker) {return new ResConfirmStickerList(confirmSticker);}
}
