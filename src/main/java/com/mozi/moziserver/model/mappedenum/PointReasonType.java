package com.mozi.moziserver.model.mappedenum;
import java.util.Set;
public enum PointReasonType {
    CHALLENGE_CONFIRM,
    CHALLENGE_EXTRA_POINT,
    ISLAND_OPEN;
    public static Set<PointReasonType> plusReason = Set.of(CHALLENGE_CONFIRM, CHALLENGE_EXTRA_POINT);
}