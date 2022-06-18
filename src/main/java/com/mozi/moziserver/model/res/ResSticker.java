package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Sticker;
import lombok.Getter;

@Getter
public class ResSticker {
    private String imgUrl;

    private Long seq;

    private ResSticker(Sticker sticker) {
        this.imgUrl=sticker.getImgUrl();

        this.seq=sticker.getSeq();
    }


    public static ResSticker of(Sticker sticker) {return new ResSticker(sticker);}
}
