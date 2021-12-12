package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.User;
import lombok.Getter;

@Getter
public class ResUser {
    private Long seq;

    private ResUser(User user){
        this.seq = user.getSeq();
    }

    public static ResUser of(User user) { return new ResUser(user); }
}
