package com.mozi.moziserver.model.req;

import com.mozi.moziserver.model.mappedenum.ConfirmReportType;
import lombok.Getter;

import javax.validation.constraints.NotNull;


@Getter
public class ReqDeclarationCreate {

    @NotNull
    private ConfirmReportType type;
}
