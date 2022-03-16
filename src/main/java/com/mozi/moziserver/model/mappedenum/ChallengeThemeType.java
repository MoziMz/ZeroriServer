package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeThemeType {
    REUSE("다시쓰기"),
    DO_NOT_USE("쓰지않기"),
    THROW_OUT("잘버리기");

    private final String name;
}