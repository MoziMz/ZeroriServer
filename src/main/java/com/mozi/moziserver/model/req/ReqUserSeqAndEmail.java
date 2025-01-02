package com.mozi.moziserver.model.req;

import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
public class ReqUserSeqAndEmail {

    @NotNull
    Long userSeq;

    @NotBlank
    String email;
}
