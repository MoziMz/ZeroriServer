package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ResUserChallenge {
    private final Long seq;
    private final UserChallengeStateType state;
    private final LocalDate startDate;
    private final List<ResUserChallengeResult> resultList;
    private final Integer totalConfirmCnt;
    private final Integer acquisitionPoints;
    private final ResUser user;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private ResUserChallenge(UserChallenge userChallenge) {
        this.seq = userChallenge.getSeq();
        this.state = userChallenge.getState();
        this.startDate = userChallenge.getStartDate();
        this.resultList = Optional.ofNullable(userChallenge.getResultList())
                .stream()
                .flatMap(Collection::stream)
                .map(ResUserChallengeResult::of)
                .collect(Collectors.toList());
        this.totalConfirmCnt = userChallenge.getTotalConfirmCnt();
        this.acquisitionPoints = userChallenge.getAcquisitionPoints();
        this.user = ResUser.of(userChallenge.getUser());
        this.createdAt = userChallenge.getCreatedAt();
        this.updatedAt = userChallenge.getUpdatedAt();
    }

    public static ResUserChallenge of(UserChallenge userChallenge) { return new ResUserChallenge(userChallenge); }
}
