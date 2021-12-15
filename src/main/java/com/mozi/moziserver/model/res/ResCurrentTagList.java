package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.CurrentTagList;
import lombok.Getter;

@Getter
public class ResCurrentTagList {
    private String name;

    private ResCurrentTagList(CurrentTagList currentTagList) {
        this.name = currentTagList.getName();
    }
    public static ResCurrentTagList of(CurrentTagList currentTagList) {return new ResCurrentTagList(currentTagList);}
}
