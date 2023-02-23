package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeTheme;
import com.mozi.moziserver.model.mappedenum.ChallengeStateType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminResChallengeList {
    private final Long seq;
    private final String name;
    private final Integer recommendedCnt;
    private final String themeName;
    private final Integer point;
    private final String mainTagName;
    private final ChallengeStateType state;
    private final Integer themeSeq;

    private AdminResChallengeList(Challenge challenge, ChallengeTheme challengeTheme) {
        this.seq = challenge.getSeq();
        this.name = challenge.getName();
        this.recommendedCnt = challenge.getRecommendedCnt();
        this.themeName = challengeTheme.getName();
        this.point = challenge.getPoint();
        this.mainTagName = challenge.getMainTag().getName();
        this.state = challenge.getState();
        this.themeSeq = challengeTheme.getSeq();
    }

    public static AdminResChallengeList of(Challenge challenge, ChallengeTheme challengeTheme) {
        return new AdminResChallengeList(challenge, challengeTheme);
    }
}
