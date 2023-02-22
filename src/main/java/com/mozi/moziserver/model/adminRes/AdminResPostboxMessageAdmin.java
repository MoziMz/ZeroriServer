package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.PostboxMessageAdmin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResPostboxMessageAdmin {
    private Long seq;
    private Long userSeq;
    private String sender;
    private String content;
    private boolean checkedState;

    private AdminResPostboxMessageAdmin(PostboxMessageAdmin postboxMessageAdmin) {
        this.seq = postboxMessageAdmin.getSeq();
        this.userSeq = postboxMessageAdmin.getUser().getSeq();
        this.sender = postboxMessageAdmin.getSender();
        this.content = postboxMessageAdmin.getContent();
        this.checkedState = postboxMessageAdmin.isCheckedState();
    }

    public static AdminResPostboxMessageAdmin of(PostboxMessageAdmin postboxMessageAdmin) {
        return new AdminResPostboxMessageAdmin(postboxMessageAdmin);
    }
}
