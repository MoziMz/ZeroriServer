package com.mozi.moziserver.model.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Getter
public class ResConfirmList {

    private Long seq;

    private Long userSeq;

    private Long challengeSeq;

    private String nickName;

    @NotNull
    private LocalDate date;

    private String imgUrl;

    private String challengeName;


    private ResConfirmList(Confirm confirm) {

        this.seq=confirm.getSeq();
        this.userSeq=confirm.getUser().getSeq();
        this.challengeSeq=confirm.getChallenge().getSeq();
        this.nickName=confirm.getUser().getNickName();
        this.date=confirm.getDate();
        this.imgUrl=confirm.getImgUrl();
        this.challengeName=confirm.getChallenge().getName();

    }

    public static ResConfirmList of(Confirm confirm) {return new ResConfirmList(confirm);}
}
