package com.mozi.moziserver.model;

import com.mozi.moziserver.model.mappedenum.PlanDateResultType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDate {
    private Integer turn;

    @Enumerated(EnumType.STRING)
    private PlanDateResultType result;
}

