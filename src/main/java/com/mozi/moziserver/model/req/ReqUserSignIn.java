package com.mozi.moziserver.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mozi.moziserver.common.Constant.PW_FIELD_NAME;

@Getter
@Setter
public class ReqUserSignIn {
    @NotNull
    private UserAuthType type;
    @NotBlank
    private String id;

    @JsonProperty(PW_FIELD_NAME)
    private String pw;
}