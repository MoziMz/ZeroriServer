package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.PostboxMessageAdmin;
import lombok.Getter;

@Getter
public class ResPostboxAdminList {
    private String sender;
    private String content;

    private ResPostboxAdminList(PostboxMessageAdmin postboxMessageAdmin) {
        this.sender = postboxMessageAdmin.getSender();
        this.content = postboxMessageAdmin.getContent();
    }

    public static ResPostboxAdminList of(PostboxMessageAdmin postboxMessageAdmin) {
        return new ResPostboxAdminList(postboxMessageAdmin);
    }
}
