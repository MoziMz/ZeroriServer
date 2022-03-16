package com.mozi.moziserver.model.req;

import com.mozi.moziserver.model.entity.ChallengeTag;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ReqAdminChallengeCreate {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Integer recommendedCnt;
    @NotNull
    private ChallengeTagType mainTag; // 현재는 대표태그 하나만 가져옴
//    @NotNull
//    private List<ChallengeTag> challengeTagList = new ArrayList<>(); // 챌린지, 태그 일대다 관계일때 동작 테스트를 위해 넣음
    @NotNull
    private Long themeSeq;
    @NotNull
    private Integer point;
}
