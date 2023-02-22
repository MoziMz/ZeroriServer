package com.mozi.moziserver.model.adminReq;

import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class AdminReqChallengeCreate {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Integer recommendedCnt;
    @NotNull
    private ChallengeTagType mainTag; // 현재는 대표태그 하나만 가져옴
    @NotNull
    private Long themeSeq;
    @NotNull
    private Integer point;
}
