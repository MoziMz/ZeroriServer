package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.ChallengeRecord;
import lombok.Getter;

@Getter
public class ResChallengeRecord {
    private Integer totalPlayerConfirmCnt;

    private ResChallengeRecord(ChallengeRecord challengeRecord) {
        this.totalPlayerConfirmCnt = challengeRecord.getTotalPlayerConfirmCnt();
    }

    public static ResChallengeRecord of(ChallengeRecord challengeRecord)
    {
        return new ResChallengeRecord(challengeRecord);
    }
}
