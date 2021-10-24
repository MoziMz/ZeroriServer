package com.mozi.moziserver.model.req;

import com.mozi.moziserver.model.mappedenum.UserAuthType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReqUserSignIn {
    @NotNull
    private UserAuthType type;
    @NotBlank
    private String id;
    @NotBlank
    private String pw;
}