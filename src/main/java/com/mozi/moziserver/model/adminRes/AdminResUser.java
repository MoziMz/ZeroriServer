package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.User;
import lombok.Getter;

@Getter
public class AdminResUser {

    private final Long seq;
    private final String nickName;

    private AdminResUser(User user) {
        this.seq = user.getSeq();
        this.nickName = user.getNickName();
    }

    public static AdminResUser of(User user) {
        return new AdminResUser(user);
    }
}
