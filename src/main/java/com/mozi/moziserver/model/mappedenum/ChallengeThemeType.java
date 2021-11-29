package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeThemeType {
    BEGINNER("초급"),
    INTERMEDIATE("중급"),
    ADVANCED("상급");

    private final String name;
}