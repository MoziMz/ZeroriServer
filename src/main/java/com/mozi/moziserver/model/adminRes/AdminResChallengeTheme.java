package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.ChallengeTheme;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResChallengeTheme {
    private final Integer seq;
    private final String name;
    private final String color;
    private final String inactiveColor;

    private AdminResChallengeTheme(ChallengeTheme challengeTheme) {
        this.seq = challengeTheme.getSeq();
        this.name = challengeTheme.getName();
        this.color = challengeTheme.getColor();
        this.inactiveColor = challengeTheme.getInactiveColor();
    }

    public AdminResChallengeTheme of(ChallengeTheme challengeTheme) {
        return new AdminResChallengeTheme(challengeTheme);
    }
}
