package com.mozi.moziserver.security;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.UserAuth;
import lombok.Getter;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@Getter
public class ResUserSignIn extends org.springframework.security.core.userdetails.User implements Authentication {
    private Long userSeq;

    public ResUserSignIn(UserAuth userAuth) {
        super(userAuth.getId(), Optional.ofNullable(userAuth.getPw()).orElse(""), Constant.USER_AUTHORITIES);
        this.userSeq = userAuth.getUser().getSeq();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userSeq;
    }

    @Override
    public boolean isAuthenticated() {
        return userSeq != null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return null;
    }
}
