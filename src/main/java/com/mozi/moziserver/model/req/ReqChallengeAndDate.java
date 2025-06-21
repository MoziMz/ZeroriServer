package com.mozi.moziserver.model.req;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class ReqChallengeAndDate {
    @NotNull
    private Long challengeSeq;
    @NotNull
    private LocalDate startDate;
}
