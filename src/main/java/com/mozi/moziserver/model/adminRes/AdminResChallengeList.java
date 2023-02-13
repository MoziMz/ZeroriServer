package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeTheme;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminResChallengeList {
    private final Long seq;
    private final String name;
    private final Integer recommendedCnt;
    private final String mainTagName;
    private final String themeName;
    private final Integer themeSeq;
    private final Integer point;

    private AdminResChallengeList(Challenge challenge, ChallengeTheme challengeTheme) {
        this.seq = challenge.getSeq();
        this.name = challenge.getName();
        this.recommendedCnt = challenge.getRecommendedCnt();
        this.mainTagName = challenge.getMainTag().getName();
        this.themeName = challengeTheme.getName();
        this.themeSeq = challengeTheme.getSeq();
        this.point = challenge.getPoint();
    }

    public static AdminResChallengeList of(Challenge challenge, ChallengeTheme challengeTheme) {
        return new AdminResChallengeList(challenge, challengeTheme);
    }
}
