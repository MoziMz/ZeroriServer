package com.mozi.moziserver.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class ReqChallengeAndDate {
    @NotNull
    private Long challengeSeq;
    @NotNull
    private LocalDate startDate;
}
