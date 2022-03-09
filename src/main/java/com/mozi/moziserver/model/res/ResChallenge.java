package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeStatistics;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.ChallengeDifficultyType;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Getter
public class ResChallenge {
    private String name;

    private String description;

    private Integer recommendedCnt;

    private ChallengeTagType tags;

//    private String tags;

    private Integer currentPlayerCnt;

    private Integer totalPlayerCnt;

    private Integer repeatPlayerCnt;

    private Integer repeatRate;

    private Integer totalCnt;

    private Integer point;

    private ChallengeDifficultyType difficulty;

    private LocalDateTime updatedAt;  // 가장 최신 인증 시간 (몇 분 전에 인증했어요)

    private Integer todayConfirmCnt; // 오늘 인증 횟수

    private Boolean scrab; // 스크랩 여부

    private ResUserChallenge userChallenge;

    private List<ResChallengeStatistics> statisticsList;


    private ResChallenge(Challenge challenge, Optional<UserChallenge> optionalUserChallenge, List<ChallengeStatistics> challengeStatisticsList) {
        this.name = challenge.getName();
        this.description = challenge.getDescription();
        this.recommendedCnt = challenge.getRecommendedCnt();
        this.tags = challenge.getTags();
        this.currentPlayerCnt = challenge.getCurrentPlayerCnt();
        this.totalPlayerCnt = challenge.getTotalPlayerCnt();
        this.totalCnt = challenge.getTotalCnt();
        this.repeatPlayerCnt = challenge.getRepeatPlayerCnt();
//      this.repeatRate = CalRepeatRate(challenge.getRepeatPlayerCnt(), challenge.getTotalPlayerCnt());
        this.repeatRate = challenge.getRepeatRate();
        this.point = challenge.getPoint();
        this.difficulty = challenge.getDifficulty();
        this.updatedAt = challenge.getUpdatedAt();
        this.userChallenge = optionalUserChallenge
                .map(ResUserChallenge::of)
                .orElse(null);
        this.statisticsList = Optional.ofNullable(challengeStatisticsList)
                .stream()
                .flatMap(Collection::stream)
                .map(ResChallengeStatistics::of)
                .collect(Collectors.toList());
    }

//    private Integer CalRepeatRate(Integer repeatPlayerCnt, Integer totalPlayerCnt) {
//        Integer rate = 0;
//        if (totalPlayerCnt != 0) {
//            rate = (repeatPlayerCnt / totalPlayerCnt) * 100;
//            return rate;
//        }
//        else return 0;
//    }

    public static ResChallenge of(Challenge challenge, Optional<UserChallenge> optionalUserChallenge, List<ChallengeStatistics> challengeStatisticsList) {
        return new ResChallenge(challenge, optionalUserChallenge, challengeStatisticsList);
    }
}
