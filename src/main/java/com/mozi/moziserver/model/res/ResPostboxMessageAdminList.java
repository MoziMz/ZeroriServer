package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.PostboxMessageAdmin;
import lombok.Getter;

@Getter
public class ResPostboxMessageAdminList {
    private String sender;
    private String content;

    private ResPostboxMessageAdminList(PostboxMessageAdmin postboxMessageAdmin) {
        this.sender = postboxMessageAdmin.getSender();
        this.content = postboxMessageAdmin.getContent();
    }

    public static ResPostboxMessageAdminList of(PostboxMessageAdmin postboxMessageAdmin) {
        return new ResPostboxMessageAdminList(postboxMessageAdmin);
    }
}
