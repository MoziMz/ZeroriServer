package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.Tag;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminResTag {
    private final Long seq;
    private final String name;

    private AdminResTag(Tag tag) {
        this.seq = tag.getSeq();
        this.name = tag.getName();
    }

    public static AdminResTag of(Tag tag) {
        return new AdminResTag(tag);
    }
}
