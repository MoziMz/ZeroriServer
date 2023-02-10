package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;

import java.util.Set;

@Getter
public enum ConfirmStateType {
    ACTIVE,
    REPORTED,
    BLOCKED,
    DELETED;

    public static Set<ConfirmStateType> activeTypes = Set.of(ACTIVE, REPORTED);
}
