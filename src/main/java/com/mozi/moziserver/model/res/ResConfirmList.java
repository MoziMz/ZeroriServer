package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;

import java.util.Date;

@Getter
public class ResConfirmList {

    private String nickName;

    private Date date;

    private String imgUrl;


    private ResConfirmList(Confirm confirm) {

        this.nickName=confirm.getId().getUser().getNickName();
        this.date=confirm.getId().getDate();
        this.imgUrl=confirm.getImgUrl();

    }

    public static ResConfirmList of(Confirm confirm) {return new ResConfirmList(confirm);}
}
