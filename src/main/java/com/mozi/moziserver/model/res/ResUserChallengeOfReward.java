package com.mozi.moziserver.model.res;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ResUserChallengeOfReward {
    // TODO res 멤버변수 final 수정하기
    private final Long seq;
    private final Long challengeSeq;
    private final UserChallengeStateType state;
    private final LocalDate startDate;
    private final List<ResUserChallengeResult> resultList;
    private final String challengeName;
    private final Long themeSeq;
    private final Integer recommendedCnt;
    private final Integer totalPoint;

    private ResUserChallengeOfReward(UserChallenge userChallenge) {
        this.seq = userChallenge.getSeq();
        this.challengeSeq = userChallenge.getChallenge().getSeq();
        this.state = userChallenge.getState();
        this.startDate = userChallenge.getStartDate();
        this.resultList = Optional.ofNullable(userChallenge.getResultList())
                .stream()
                .flatMap(Collection::stream)
                .map(ResUserChallengeResult::of)
                .collect(Collectors.toList());
        this.challengeName = userChallenge.getChallenge().getName();
        this.themeSeq = userChallenge.getChallenge().getThemeSeq();
        this.recommendedCnt = userChallenge.getChallenge().getRecommendedCnt();
        int totalPoint=userChallenge.getAcquisitionPoints();
        if(userChallenge.getTotalConfirmCnt().equals(userChallenge.getChallenge().getRecommendedCnt())){
            totalPoint=userChallenge.getAcquisitionPoints()+Constant.challengeExtraPoints;
        }
        this.totalPoint=totalPoint;

    }

    public static ResUserChallengeOfReward of(UserChallenge userChallenge) {
        return new ResUserChallengeOfReward(userChallenge);
    }
}
