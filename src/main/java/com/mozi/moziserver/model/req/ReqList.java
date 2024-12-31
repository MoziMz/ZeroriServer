package com.mozi.moziserver.model.req;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Getter
@Setter
public class ReqList {
    @Min(1L)
    private Long prevLastSeq;

    @Min(1L) @Max(30L)
    private Integer pageSize = 20;
}
