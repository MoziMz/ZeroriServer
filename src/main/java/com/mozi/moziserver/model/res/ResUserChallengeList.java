package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ResUserChallengeList {
    private final Long seq;
    private final Long challengeSeq;
    private final UserChallengeStateType state;
    private final LocalDate startDate;
    private final List<ResUserChallengeResult> resultList;
    private final String challengeName;
    private final ChallengeTagType tags;
    private final Integer recommendedCnt;

    private ResUserChallengeList(UserChallenge userChallenge) {
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
        this.tags = userChallenge.getChallenge().getTags();
//                Optional.ofNullable(userChallenge.getChallenge().getTagList())
//                .filter(list -> !CollectionUtils.isEmpty(list))
//                .flatMap(list -> list.stream().findFirst())
//                .orElse(null);
        this.recommendedCnt = userChallenge.getChallenge().getRecommendedCnt();
    }

    public static ResUserChallengeList of(UserChallenge userChallenge) {
        return new ResUserChallengeList(userChallenge);
    }
}
