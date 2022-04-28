package com.mozi.moziserver.model.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class ReqUserChallengeCreate {
    @NotNull
    private Long challengeSeq;

    @NotNull
    private LocalDate startDate;

    @JsonIgnore
    LocalDate today = LocalDate.now();
}
