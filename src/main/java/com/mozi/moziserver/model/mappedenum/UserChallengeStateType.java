package com.mozi.moziserver.model.mappedenum;

import java.util.Set;

public enum UserChallengeStateType {
    END,
    PLAN,
    DOING;

    public static Set<UserChallengeStateType> activeTypes = Set.of(PLAN, DOING);
}
