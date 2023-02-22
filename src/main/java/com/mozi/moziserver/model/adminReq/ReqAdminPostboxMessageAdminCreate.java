package com.mozi.moziserver.model.adminReq;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReqAdminPostboxMessageAdminCreate {
    @NotNull
    private Long userSeq;
    @NotBlank
    private String sender;
    @NotBlank
    private String content;
}
