package com.mozi.moziserver.model.req;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ReqUserSeqAndEmail {

    @NotNull
    Long userSeq;

    @NotBlank
    String email;
}
