package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.CurrentTagList;
import lombok.Getter;

@Getter
public class ResCurrentTagList {
    private Long seq;
    private String name;

    private ResCurrentTagList(CurrentTagList currentTagList) {
        this.seq = currentTagList.getSeq();
        this.name = currentTagList.getTag().getName();
    }
    public static ResCurrentTagList of(CurrentTagList currentTagList) {return new ResCurrentTagList(currentTagList);}
}
