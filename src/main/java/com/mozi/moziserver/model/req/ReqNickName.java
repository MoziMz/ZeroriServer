package com.mozi.moziserver.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ReqNickName {
    @NotBlank
    private String nickName;
}
