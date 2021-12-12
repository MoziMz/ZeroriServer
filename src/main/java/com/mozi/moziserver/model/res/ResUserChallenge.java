package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ResUserChallenge {
    private Long seq;
    private UserChallengeStateType state;
    private LocalDate startDate;
    private String planDate;
    private Integer totalConfirmCnt;
    private ResUser user;
//    private ResChallenge challenge;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ResUserChallenge(UserChallenge userChallenge) {
        this.seq = userChallenge.getSeq();
        this.state = userChallenge.getState();
        this.startDate = userChallenge.getStartDate();
        this.planDate = userChallenge.getPlanDate();
        this.totalConfirmCnt = userChallenge.getTotalConfirmCnt();
        this.user = ResUser.of(userChallenge.getUser());
//        this.challenge = ResChallenge.of(userChallenge.getChallenge());
        this.createdAt = userChallenge.getCreatedAt();
        this.updatedAt = userChallenge.getUpdatedAt();
    }

    public static ResUserChallenge of(UserChallenge userChallenge) { return new ResUserChallenge(userChallenge); }
}
