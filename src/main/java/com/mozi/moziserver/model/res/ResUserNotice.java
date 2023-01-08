package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.UserNotice;
import com.mozi.moziserver.model.mappedenum.UserNoticeType;
import lombok.Getter;


@Getter
public class ResUserNotice {

    private final UserNoticeType userNoticeType;
    private final Long contentSeq;

    private ResUserNotice(UserNotice userNotice){
        this.userNoticeType = userNotice.getType();
        this.contentSeq = userNotice.getContentSeq();
    }

    public static ResUserNotice of(UserNotice userNotice){
        return new ResUserNotice(userNotice);
    }
}
