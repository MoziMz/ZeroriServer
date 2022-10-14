package com.mozi.moziserver.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ResUserRememberSignIn implements Authentication {
    private ResUserSignIn resUserSignIn;

    public ResUserRememberSignIn(ResUserSignIn resUserSignIn) {
        this.resUserSignIn = resUserSignIn;
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
