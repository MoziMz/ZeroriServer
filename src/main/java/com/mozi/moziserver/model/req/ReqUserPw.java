package com.mozi.moziserver.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mozi.moziserver.common.Constant.PW_FIELD_NAME;

@Getter
@Setter
public class ReqUserPw {
    @NotBlank
    @JsonProperty(PW_FIELD_NAME)
    private String pw;
}
