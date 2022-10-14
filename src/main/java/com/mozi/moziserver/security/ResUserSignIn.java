package com.mozi.moziserver.security;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.UserAuth;
import lombok.Getter;

import java.util.Optional;

@Getter
public class ResUserSignIn extends org.springframework.security.core.userdetails.User {
    private Long userSeq;
    private Long userAuthSeq;

    public ResUserSignIn(UserAuth userAuth) {
        super(userAuth.getId(), Optional.ofNullable(userAuth.getPw()).orElse(""), Constant.USER_AUTHORITIES);
        this.userSeq = userAuth.getUser().getSeq();
        this.userAuthSeq = userAuth.getSeq();
    }
}
