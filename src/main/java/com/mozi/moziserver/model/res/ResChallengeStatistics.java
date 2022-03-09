package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.ChallengeStatistics;
import lombok.Getter;

@Getter
public class ResChallengeStatistics {
    private Integer year;
    private Integer month;
    private Integer playerFirstTryingCnt;
    private Integer playerConfirmCnt;

    private ResChallengeStatistics(ChallengeStatistics challengeStatistics){
        this.year = challengeStatistics.getYear();
        this.month = challengeStatistics.getMonth();
        this.playerFirstTryingCnt = challengeStatistics.getPlayerFirstTryingCnt();
        this.playerConfirmCnt = challengeStatistics.getPlayerConfirmCnt();
    }

    public static ResChallengeStatistics of(ChallengeStatistics challengeStatistics) {
        return new ResChallengeStatistics(challengeStatistics);
    }

}
