package com.mozi.moziserver.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class ReqUserChallengeList {
    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Long ChallengeSeq;
}
