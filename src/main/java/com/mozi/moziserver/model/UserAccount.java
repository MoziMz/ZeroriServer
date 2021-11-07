package com.mozi.moziserver.model;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.UserAuth;
import lombok.Getter;

@Getter
public class UserAccount extends org.springframework.security.core.userdetails.User {
    private Long userSeq;

    public UserAccount(UserAuth userAuth) {
        super(userAuth.getId(), userAuth.getPw(), Constant.USER_AUTHORITIES);
        this.userSeq = userAuth.getUser().getSeq();
    }
}
