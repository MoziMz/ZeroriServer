package com.mozi.moziserver.model.adminReq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mozi.moziserver.common.Constant.PW_FIELD_NAME;

@Getter
@Setter
public class ReqAdminSignIn {

    @NotBlank
    private String id;

    @JsonProperty(PW_FIELD_NAME)
    private String pw;
}
