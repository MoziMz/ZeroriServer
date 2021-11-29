package com.mozi.moziserver.model.req;

import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import com.mozi.moziserver.model.mappedenum.ChallengeThemeType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class ReqChallengeList {
    private ChallengeTagType challengeTagType;

    private ChallengeThemeType challengeThemeType;

    @Min(1L)
    private Long prevLastPostSeq;

    @Min(1L) @Max(50L)
    private Integer pageSize = 20;
}
