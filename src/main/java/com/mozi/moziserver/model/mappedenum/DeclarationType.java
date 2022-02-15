package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeclarationType {
    SEXUAL_CONTENT("성적인 콘텐츠"),
    HATE_SPEECH("혐오 발언,상징"),
    VIOLENT_CONTENT("폭력적 콘텐츠"),
    FALSE_INFORMATION("거짓정보"),
    ETC("기타");

    private final String name;
}
