package com.mozi.moziserver.security;

import com.mozi.moziserver.model.entity.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class RememberMeAuthentication extends com.mozi.moziserver.security.UserAuthentication {
    private final WebAuthenticationDetails details;

    public RememberMeAuthentication(User user, WebAuthenticationDetails details) {
        super(user);
        this.details = details;
    }

    @Override
    public Object getDetails() {
        return details;
    }
}
