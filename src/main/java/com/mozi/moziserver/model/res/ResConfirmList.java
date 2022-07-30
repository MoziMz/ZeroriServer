package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Confirm;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResConfirmList {
    private Long seq;
    private Long userSeq;
    private Long challengeSeq;
    private String nickName;
    private LocalDate date;
    private String imgUrl;
    private String challengeName;
    private Long challengeThemeSeq ;
    private Integer likeCnt;
    private boolean isLiked;
    private List<ResConfirmStickerList> resConfirmStickerList;

    private ResConfirmList(Confirm confirm) {
        this.seq=confirm.getSeq();
        this.userSeq=confirm.getUser().getSeq();
        this.challengeSeq=confirm.getChallenge().getSeq();
        this.nickName=confirm.getUser().getNickName();
        this.date=confirm.getDate();
        this.imgUrl=confirm.getImgUrl();
        this.challengeName=confirm.getChallenge().getName();
        this.challengeThemeSeq = confirm.getChallenge().getThemeSeq();
        this.likeCnt = confirm.getLikeCnt();
        this.resConfirmStickerList = confirm.getConfirmStickerList()
                .stream()
                .map(ResConfirmStickerList::of)
                .collect(Collectors.toList());
        this.isLiked = confirm.isLiked();
    }

    public static ResConfirmList of(Confirm confirm) { return new ResConfirmList(confirm); }
}
