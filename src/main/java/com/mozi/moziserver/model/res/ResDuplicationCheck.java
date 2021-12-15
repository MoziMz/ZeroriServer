package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.User;
import lombok.Getter;

@Getter
public class ResDuplicationCheck {
    private String nickName;
    private String email;

    private ResDuplicationCheck(User user) {
        this.nickName = user.getNickName();
        this.email = user.getEmail();
    }

    public static ResDuplicationCheck of(User user) {
        return new ResDuplicationCheck(user);
    }
}
