package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnimalType {
    LION("사자"),
    PANDA("판다"),
    RABBIT("토끼"),
    MONKEY("원숭이");

    private final String name;
}
