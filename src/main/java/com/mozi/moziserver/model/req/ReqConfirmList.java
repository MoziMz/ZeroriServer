package com.mozi.moziserver.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class ReqConfirmList {

    @Min(1L)
    private Long prevLastConfirmSeq;

    @Min(1L) @Max(50L)
    private Integer pageSize = 20;
}
