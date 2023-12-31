package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.UserNotice;
import com.mozi.moziserver.model.mappedenum.UserNoticeType;
import lombok.Getter;


@Getter
public class ResUserNotice {

    private final Long relatedSeq;

    private ResUserNotice(UserNotice userNotice){
        this.relatedSeq = userNotice.getRelatedSeq();
    }

    public static ResUserNotice of(UserNotice userNotice){
        return new ResUserNotice(userNotice);
    }
}
