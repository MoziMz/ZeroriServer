package com.mozi.moziserver.model.req;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class ReqTestUserChallengeStartDateUpdate {
    @NotNull
    private LocalDate startDate;
}
