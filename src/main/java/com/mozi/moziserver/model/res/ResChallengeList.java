package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;


@Getter
public class ResChallengeList {
    private Long seq;

    private String name;

    private String description;

    private Integer recommendedCnt;

    private Long themeSeq;

    private ChallengeTagType mainTag;

    private Boolean scraped;

    private ResChallengeList(Challenge challenge,Boolean scrapStatus) {
        this.seq = challenge.getSeq();
        this.name = challenge.getName();
        this.description = challenge.getDescription();
        this.recommendedCnt = challenge.getRecommendedCnt();
        this.themeSeq = challenge.getThemeSeq();
        this.mainTag = challenge.getMainTag();
        this.scraped=scrapStatus;
    }

    public static ResChallengeList of(Challenge challenge,Boolean scraped) {return new ResChallengeList(challenge,scraped);}
}
