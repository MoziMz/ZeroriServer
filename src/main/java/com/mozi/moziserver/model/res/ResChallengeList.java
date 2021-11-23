package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Challenge;
import lombok.Getter;

@Getter
public class ResChallengeList {
    private String name;

    private String description;

    private Integer recommendedCnt;

    private String tags;

    private String currentPlayerCnt;

    private String imgUrl; // 가장 최근 인증한 사람의 프로필 이미지

    private Boolean scrab; // 스크랩 여부


    private ResChallengeList(Challenge challenge) {
        this.name = challenge.getName();
        this.description = challenge.getDescription();
        this.recommendedCnt = challenge.getRecommendedCnt();
        this.tags = challenge.getTags();
        this.currentPlayerCnt = challenge.getCurrentPlayerCnt();
    }

    public static ResChallengeList of(Challenge challenge) {return new ResChallengeList(challenge);}
}
