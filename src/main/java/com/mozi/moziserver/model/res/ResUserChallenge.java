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
    private Long seq;
    private UserChallengeStateType state;
    private LocalDate startDate;
    private List<ResPlanDate> planDateList;
    private Integer totalConfirmCnt;
    private ResUser user;
//    private ResChallenge challenge;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
//        this.challenge = ResChallenge.of(userChallenge.getChallenge());
        this.createdAt = userChallenge.getCreatedAt();
        this.updatedAt = userChallenge.getUpdatedAt();
    }

    public static ResUserChallenge of(UserChallenge userChallenge) { return new ResUserChallenge(userChallenge); }
}
