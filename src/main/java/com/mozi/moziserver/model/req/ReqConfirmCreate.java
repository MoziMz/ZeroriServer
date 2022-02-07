package com.mozi.moziserver.model.req;

import com.mozi.moziserver.model.mappedenum.QuestionCategory;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
public class ReqConfirm {
    @NotNull
    private Date date;

    @NotBlank
    private String imgUrl;
}
