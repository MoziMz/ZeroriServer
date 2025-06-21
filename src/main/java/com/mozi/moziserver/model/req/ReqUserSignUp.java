package com.mozi.moziserver.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.mozi.moziserver.common.Constant.PW_FIELD_NAME;

@Getter
@Setter
public class ReqUserSignUp {
    @NotNull
    private UserAuthType type;
    @NotBlank
    private String id;
    @NotBlank
    @JsonProperty(PW_FIELD_NAME)
    private String pw;
}