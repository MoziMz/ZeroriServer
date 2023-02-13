package com.mozi.moziserver.model.adminReq;

import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqAdminChallengeUpdate {

    private String name;

    private String description;

    private Integer recommendedCnt;

    private ChallengeTagType mainTag;

    private Long themeSeq;

    private Integer point;
}
