package com.mozi.moziserver.model.req;

import com.mozi.moziserver.model.mappedenum.QuestionCategory;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ReqQuestionCreate {

    private String email;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private QuestionCategory questionCategory;

    @NotBlank
    private String imgUrl;
}
