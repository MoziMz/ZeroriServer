package com.mozi.moziserver.model.adminReq;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class AdminReqCurrentThemeList {
    @NotNull
    private Long seq;
    @NotNull
    private Integer turn;
    @NotNull
    private Integer challengeThemeSeq;
}
