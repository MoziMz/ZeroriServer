package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.UserChallengeRecord;
import lombok.Getter;

@Getter
public class ResUserChallengeRecord {
    private final Integer confirmCnt;
    private final Integer acquisitionPoint;

    private ResUserChallengeRecord(UserChallengeRecord userChallengeRecord) {
        this.confirmCnt = userChallengeRecord.getConfirmCnt();
        this.acquisitionPoint = userChallengeRecord.getAcquisitionPoint();
    }

    public static ResUserChallengeRecord of(UserChallengeRecord userChallengeRecord) {
        return new ResUserChallengeRecord(userChallengeRecord);
    }
}
