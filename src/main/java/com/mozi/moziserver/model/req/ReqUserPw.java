package com.mozi.moziserver.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mozi.moziserver.common.Constant.CURRENT_PW_FIELD_NAME;
import static com.mozi.moziserver.common.Constant.PW_FIELD_NAME;

@Getter
@Setter
public class ReqUserPw {
    @JsonProperty(PW_FIELD_NAME)
    private String newPw;

    @NotBlank
    @JsonProperty(CURRENT_PW_FIELD_NAME)
    private String currentPw;
}
