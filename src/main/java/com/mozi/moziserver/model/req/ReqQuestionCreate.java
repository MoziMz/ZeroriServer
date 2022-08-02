package com.mozi.moziserver.model.req;

import com.mozi.moziserver.model.mappedenum.QuestionCategory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReqQuestionCreate {

    private String email;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private QuestionCategory questionCategory;

    private MultipartFile image;
}
