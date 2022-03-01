package com.mozi.moziserver.model.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmSticker;
import com.mozi.moziserver.model.entity.PreparationItem;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResConfirm {
    private String nickName;

    @NotNull
    private LocalDate date;

    private String imgUrl;

    private String challengeName;

    private ChallengeTagType challengeTags;

    private List<ResConfirmStickerList> resConfirmStickerList;

    private ResConfirm(Confirm confirm,List<ConfirmSticker> confirmStickerList) {

        this.nickName=confirm.getUser().getNickName();
        this.date=confirm.getDate();
        this.imgUrl=confirm.getImgUrl();
        this.challengeName=confirm.getChallenge().getName();
        this.challengeTags=confirm.getChallenge().getTags();
        this.resConfirmStickerList=confirmStickerList.stream().map(ResConfirmStickerList::of).collect(Collectors.toList());

    }

    public static ResConfirm of(Confirm confirm,List<ConfirmSticker> confirmStickerList) {return new ResConfirm(confirm,confirmStickerList);}
}
