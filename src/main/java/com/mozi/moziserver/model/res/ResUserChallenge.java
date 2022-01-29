package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.PlanDate;
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
    private final List<ResPlanDate> planDateList;
    private final Integer totalConfirmCnt;
    private final ResUser user;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private ResUserChallenge(UserChallenge userChallenge) {
        this.seq = userChallenge.getSeq();
        this.state = userChallenge.getState();
        this.startDate = userChallenge.getStartDate();
        this.planDateList = Optional.ofNullable(userChallenge.getPlanDateList())
                .stream()
                .flatMap(Collection::stream)
                .map(ResPlanDate::of)
                .collect(Collectors.toList());
        this.totalConfirmCnt = userChallenge.getTotalConfirmCnt();
        this.user = ResUser.of(userChallenge.getUser());
        this.createdAt = userChallenge.getCreatedAt();
        this.updatedAt = userChallenge.getUpdatedAt();
    }

    public static ResUserChallenge of(UserChallenge userChallenge) { return new ResUserChallenge(userChallenge); }
}
