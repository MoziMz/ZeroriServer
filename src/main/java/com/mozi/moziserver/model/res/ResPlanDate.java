package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.PlanDate;
import com.mozi.moziserver.model.mappedenum.PlanDateResultType;
import lombok.Getter;

@Getter
public class ResPlanDate {
    private Integer turn;
    private PlanDateResultType result;

    private ResPlanDate(PlanDate planDate){
        this.turn = planDate.getTurn();
        this.result = planDate.getResult();
    }

    public static ResPlanDate of(PlanDate planDate) {
        return new ResPlanDate(planDate);
    }
}
