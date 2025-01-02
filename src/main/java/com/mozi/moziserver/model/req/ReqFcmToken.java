package com.mozi.moziserver.model.req;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class ReqFcmToken {
    @NotBlank
    private String deviceId;
    @NotBlank
    private String token;
}
