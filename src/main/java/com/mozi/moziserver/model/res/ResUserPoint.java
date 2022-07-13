package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.UserReward;
import lombok.Getter;

@Getter
public class ResUserPoint {
    private Integer point;

    private ResUserPoint(UserReward userReward) {
        this.point = userReward.getPoint();
    }

    public static ResUserPoint of(UserReward userReward) {
        return new ResUserPoint(userReward);
    }
}
