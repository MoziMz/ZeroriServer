package com.mozi.moziserver.model.req;

import com.mozi.moziserver.model.mappedenum.ResignReasonType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReqResign {
    private List<ResignReasonType> resignReasonTypeList = new ArrayList<>();
}
