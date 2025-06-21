package com.mozi.moziserver.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

import static com.mozi.moziserver.common.Constant.PW_FIELD_NAME;

@Getter
@Setter
public class ReqUserNickNameAndPw {
    @NotBlank
    private String nickName;

    @NotBlank
    @JsonProperty(PW_FIELD_NAME)
    private String pw;
}
