package com.mozi.moziserver.model.req;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class ReqSuggestionCreate {

    @NotBlank
    private String challengeName;

    @NotBlank
    private String explanation;
}
