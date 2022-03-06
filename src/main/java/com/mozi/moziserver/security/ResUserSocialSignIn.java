package com.mozi.moziserver.security;

import com.mozi.moziserver.model.entity.UserAuth;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ResUserSocialSignIn implements Authentication {
    private ResUserSignIn resUserSignIn;

    public ResUserSocialSignIn(UserAuth userAuth) {
        this.resUserSignIn = new ResUserSignIn(userAuth);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return resUserSignIn.getAuthorities();
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
        return resUserSignIn;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return null;
    }
}
