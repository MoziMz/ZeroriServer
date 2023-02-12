package com.mozi.moziserver.model.req;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class ReqConfirmCreate {
    @NotNull
    private LocalDate date;

    @NotBlank
    private String imgUrl;
}
