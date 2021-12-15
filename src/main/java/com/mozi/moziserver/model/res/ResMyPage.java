package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.UserAuth;
import lombok.Getter;

@Getter
public class ResMyPage {
    private String nickName;
    private String email;

    private ResMyPage(UserAuth userAuth){
        this.nickName = userAuth.getUser().getNickName();
        this.email = userAuth.getUser().getEmail();
    }

    public static ResMyPage of(UserAuth userAuth) {
        return new ResMyPage(userAuth);
    }
}
