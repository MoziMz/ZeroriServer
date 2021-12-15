package com.mozi.moziserver.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ReqAuthEmail {
    @NotBlank
    private String email;
}
