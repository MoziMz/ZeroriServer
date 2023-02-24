package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.ChallengeExplanation;
import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeStatistics;
import com.mozi.moziserver.model.entity.ChallengeTheme;
import com.mozi.moziserver.model.mappedenum.ChallengeStateType;
import com.mozi.moziserver.model.res.ResChallengeStatistics;
import com.mozi.moziserver.model.res.ResChallengeTagList;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class AdminResChallenge {
    private final Long seq;
    private final String name;
    private final Integer recommendedCnt;
    private final String themeName;
    private final Integer point;
    private final String mainTagName; // 현재는 챌린지당 하나의 태그만 있다.
    private final ChallengeStateType state;
    private final ChallengeExplanation explanation;
    private final List<ResChallengeTagList> tagList;
    private final List<ResChallengeStatistics> statisticsList;

    private AdminResChallenge(Challenge challenge, ChallengeTheme challengeTheme, List<ChallengeStatistics> challengeStatisticsList) {
        this.seq = challenge.getSeq();
        this.name = challenge.getName();
        this.recommendedCnt = challenge.getRecommendedCnt();
        this.themeName = challengeTheme.getName();
        this.point = challenge.getPoint();
        this.mainTagName = challenge.getMainTag().getName();
        this.state = challenge.getState();
        this.explanation = challenge.getExplanation();
        this.tagList = challenge.getChallengeTagList().stream().map(ResChallengeTagList::of).collect(Collectors.toList());
        this.statisticsList = Optional.ofNullable(challengeStatisticsList)
                .stream()
                .flatMap(Collection::stream)
                .map(ResChallengeStatistics::of)
                .collect(Collectors.toList());
    }

    public static AdminResChallenge of(Challenge challenge, ChallengeTheme challengeTheme, List<ChallengeStatistics> challengeStatisticsList) {
        return new AdminResChallenge(challenge, challengeTheme, challengeStatisticsList);
    }
}
