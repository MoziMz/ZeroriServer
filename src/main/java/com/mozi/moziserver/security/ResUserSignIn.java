package com.mozi.moziserver.security;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserRoleType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ResUserSignIn extends org.springframework.security.core.userdetails.User {
    private Long userSeq;
    private Long userAuthSeq;

    public ResUserSignIn(UserAuth userAuth) {
        super(userAuth.getId(), Optional.ofNullable(userAuth.getPw()).orElse(""),
                userAuth.getRoleList().stream().map(UserRoleType::name).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        this.userSeq = userAuth.getUser().getSeq();
        this.userAuthSeq = userAuth.getSeq();
    }
}
