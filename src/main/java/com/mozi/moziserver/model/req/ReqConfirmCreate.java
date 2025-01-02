package com.mozi.moziserver.model.req;

import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class ReqConfirmCreate {
    @NotNull
    private LocalDate date;

    @NotBlank
    private String imgUrl;
}
