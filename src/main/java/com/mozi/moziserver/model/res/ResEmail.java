package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.UserAuth;
import lombok.Getter;

@Getter
public class ResEmail {
    String email;
    private ResEmail(UserAuth userAuth) {
        this.email = userAuth.getId();
    }
    public static ResEmail of(UserAuth userAuth){
        return new ResEmail(userAuth);
    }
}
