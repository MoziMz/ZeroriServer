package com.mozi.moziserver.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class UserRememberAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof ResUserRememberSignIn))
            return null;

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(ResUserRememberSignIn.class);
    }
}
