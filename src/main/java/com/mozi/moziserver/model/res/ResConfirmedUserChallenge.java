package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.UserChallengeRecord;
import lombok.Getter;
import lombok.Setter;

import static com.mozi.moziserver.model.entity.QUserChallengeRecord.userChallengeRecord;

@Getter
public class ResConfirmedUserChallenge {

    private Long challengeSeq;

    private String challengeName;

    private Integer totalConfirmCnt=0;

    private ResConfirmedUserChallenge(UserChallengeRecord userChallengeRecord) {

        this.challengeSeq=userChallengeRecord.getChallenge().getSeq();

        this.challengeName=userChallengeRecord.getChallenge().getName();

        this.totalConfirmCnt= userChallengeRecord.getConfirmCnt();

    }



    public static ResConfirmedUserChallenge of(UserChallengeRecord userChallengeRecord) {return new ResConfirmedUserChallenge(userChallengeRecord);}
}
