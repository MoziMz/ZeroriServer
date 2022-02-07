package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.UserStickerImg;
import lombok.Getter;

@Getter
public class ResStickerList {

    private String imgUrl;

    private Boolean download;

    private Long seq;

    private ResStickerList(UserStickerImg userStickerImg) {
        this.imgUrl=userStickerImg.getStickerImg().getImgUrl();

        this.download=userStickerImg.isDownloaded();

        this.seq=userStickerImg.getStickerImg().getSeq();
    }


    public static ResStickerList of(UserStickerImg userStickerImg) {return new ResStickerList(userStickerImg);}

}
