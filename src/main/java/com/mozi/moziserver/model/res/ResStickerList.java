package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.StickerImg;
import com.mozi.moziserver.model.entity.UserStickerImg;
import lombok.Getter;

@Getter
public class ResStickerList {
    private String imgUrl;
    private Long seq;

    private ResStickerList(StickerImg stickerImg) {
        this.imgUrl=stickerImg.getImgUrl();
        this.seq=stickerImg.getSeq();
    }

    public static ResStickerList of(StickerImg stickerImg) { return new ResStickerList(stickerImg); }
}
