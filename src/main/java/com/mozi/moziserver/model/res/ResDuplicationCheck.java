package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import lombok.Getter;

@Getter
public class ResDuplicationCheck {
    private String nickName;
    private String email;
    private String id;

    private ResDuplicationCheck(UserAuth userAuth) {
        this.nickName = userAuth.getUser().getNickName();
        this.email = userAuth.getUser().getEmail();
        this.id = userAuth.getId();
    }

    public static ResDuplicationCheck of(UserAuth userAuth) {
        return new ResDuplicationCheck(userAuth);
    }
}
