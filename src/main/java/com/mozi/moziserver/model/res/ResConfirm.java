package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmSticker;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResConfirm {
    private String nickName;

    private LocalDateTime createdAt;

    private String imgUrl;

    private String challengeName;

    private Long challengeThemeSeq ;

    private List<ResConfirmStickerList> resConfirmStickerList;

    private ResConfirm(Confirm confirm,List<ConfirmSticker> confirmStickerList) {

        this.nickName=confirm.getUser().getNickName();
        this.createdAt = confirm.getCreatedAt();
        this.imgUrl=confirm.getImgUrl();
        this.challengeName=confirm.getChallenge().getName();
        this.challengeThemeSeq=confirm.getChallenge().getThemeSeq();
        this.resConfirmStickerList=confirmStickerList.stream().map(ResConfirmStickerList::of).collect(Collectors.toList());

    }

    public static ResConfirm of(Confirm confirm,List<ConfirmSticker> confirmStickerList) {return new ResConfirm(confirm,confirmStickerList);}
}
