package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.UserChallengeRecord;
import lombok.Getter;

@Getter
public class ResConfirmedUserChallengeRecord {

    private Long challengeSeq;

    private String challengeName;

    private Integer totalConfirmCnt=0;

    private ResConfirmedUserChallengeRecord(UserChallengeRecord userChallengeRecord) {

        this.challengeSeq=userChallengeRecord.getChallenge().getSeq();

        this.challengeName=userChallengeRecord.getChallenge().getName();

        this.totalConfirmCnt= userChallengeRecord.getConfirmCnt();

    }



    public static ResConfirmedUserChallengeRecord of(UserChallengeRecord userChallengeRecord) {return new ResConfirmedUserChallengeRecord(userChallengeRecord);}
}
