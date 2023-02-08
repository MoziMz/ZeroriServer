package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailAuthResultState {
    SUCCESS,
    ALREADY_SUCCESS,
    EXPIRATION;

}
