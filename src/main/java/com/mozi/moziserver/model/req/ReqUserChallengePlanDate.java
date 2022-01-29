package com.mozi.moziserver.model.req;

import com.mozi.moziserver.model.PlanDate;
import com.mozi.moziserver.model.mappedenum.PlanDateResultType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ReqUserChallengePlanDate {
    @NotBlank
    private Integer turn;
    @NotBlank
    //@Pattern(regexp = "(http|https)://(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(/|/([\\w#!:.?+=&%@!\\-/]))?")
    private PlanDateResultType planDateType;

    public PlanDate toPlanDate() {
        return PlanDate.builder()
                .turn(turn)
                .result(planDateType)
                .build();
    }
}
