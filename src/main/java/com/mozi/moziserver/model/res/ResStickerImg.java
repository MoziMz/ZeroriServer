package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.StickerImg;
import lombok.Getter;

@Getter
public class ResStickerImg {
    private String imgUrl;

    private Long seq;

    private ResStickerImg(StickerImg stickerImg) {
        this.imgUrl=stickerImg.getImgUrl();

        this.seq=stickerImg.getSeq();
    }


    public static ResStickerImg of(StickerImg stickerImg) {return new ResStickerImg(stickerImg);}
}
