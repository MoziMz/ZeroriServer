package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmSticker;
import com.mozi.moziserver.model.entity.PreparationItem;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResConfirm {
    private String nickName;

    private Date date;

    private String imgUrl;

    private String challengeName;

    private ChallengeTagType challengeTags;

    private List<ResConfirmStickerList> resConfirmStickerList;

    private ResConfirm(Confirm confirm,List<ConfirmSticker> confirmStickerList) {

        this.nickName=confirm.getId().getUser().getNickName();
        this.date=confirm.getId().getDate();
        this.imgUrl=confirm.getImgUrl();
        this.challengeName=confirm.getId().getChallenge().getName();
        this.challengeTags=confirm.getId().getChallenge().getTags();
        this.resConfirmStickerList=confirmStickerList.stream().map(ResConfirmStickerList::of).collect(Collectors.toList());

    }

    public static ResConfirm of(Confirm confirm,List<ConfirmSticker> confirmStickerList) {return new ResConfirm(confirm,confirmStickerList);}
}
