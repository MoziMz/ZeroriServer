package com.mozi.moziserver.model.req;

import com.mozi.moziserver.model.mappedenum.ChallengeDifficultyType;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class ReqAdminChallenge {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Integer recommendedCnt;

    private ChallengeTagType tags;

//    private String tags;

    @NotNull
    private Integer currentPlayerCnt;

    @NotNull
    private Integer totalPlayerCnt;

    @NotNull
    private Integer repeatPlayerCnt;

    @NotNull
    private Integer totalCnt;

    @NotNull
    private Integer totalChallengeConfirmCnt;

    @NotNull
    private Integer repeatRate;

    @NotNull
    private Integer point;

    private ChallengeDifficultyType difficulty;
}
