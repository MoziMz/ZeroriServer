package com.mozi.moziserver.model.req;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Getter
public class ReqUserChallengeUpdate {
    @NotNull
    private LocalDate startDate;

    @NotNull List<ReqUserChallengePlanDate> planDates = new LinkedList<>();
}
