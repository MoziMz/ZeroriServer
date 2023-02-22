package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.CurrentTagList;
import lombok.Getter;

@Getter
public class AdminResCurrentTagList {
    private final Long seq;
    private final Integer turn;
    private final Long tagSeq;

    private AdminResCurrentTagList(CurrentTagList currentTagList) {
        this.seq = currentTagList.getSeq();
        this.turn = currentTagList.getTurn();
        this.tagSeq = currentTagList.getTag().getSeq();
    }

    public static AdminResCurrentTagList of(CurrentTagList currentTagList) {
        return new AdminResCurrentTagList(currentTagList);
    }

}
