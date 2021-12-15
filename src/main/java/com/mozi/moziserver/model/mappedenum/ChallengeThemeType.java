package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeThemeType {
    초급("초급"),
    중급("중급"),
    상급("상급");

    private final String name;
}