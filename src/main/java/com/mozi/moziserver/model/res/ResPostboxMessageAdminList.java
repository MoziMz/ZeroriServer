package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.PostboxMessageAdmin;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResPostboxMessageAdminList {
    private  Long seq;
    private String sender;
    private String content;
    private boolean checkedState;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ResPostboxMessageAdminList(PostboxMessageAdmin postboxMessageAdmin) {
        this.seq = postboxMessageAdmin.getSeq();
        this.sender = postboxMessageAdmin.getSender();
        this.content = postboxMessageAdmin.getContent();
        this.checkedState = postboxMessageAdmin.isCheckedState();
        this.createdAt = postboxMessageAdmin.getCreatedAt();
        this.updatedAt = postboxMessageAdmin.getUpdatedAt();
    }

    public static ResPostboxMessageAdminList of(PostboxMessageAdmin postboxMessageAdmin) {
        return new ResPostboxMessageAdminList(postboxMessageAdmin);
    }
}
