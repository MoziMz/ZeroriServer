package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class ResChallengeList {
    private Long seq;

    private String name;

    private String description;

    private Integer recommendedCnt;

    private ChallengeTagType tags;

//    private String tags;

    private Integer currentPlayerCnt;

    private String imgUrl; // 가장 최근 인증한 사람의 프로필 이미지

    private Boolean scrab; // 스크랩 여부


    private ResChallengeList(Challenge challenge) {
        this.seq = challenge.getSeq();
        this.name = challenge.getName();
        this.description = challenge.getDescription();
        this.recommendedCnt = challenge.getRecommendedCnt();
        this.tags = challenge.getTags();
        this.currentPlayerCnt = challenge.getCurrentPlayerCnt();
    }

    public static ResChallengeList of(Challenge challenge) {return new ResChallengeList(challenge);}
}
