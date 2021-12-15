package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnimalType {
    사자("사자"),
    판다("판다"),
    토끼("토끼"),
    원숭이("원숭이");

    private final String name;
}
