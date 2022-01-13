package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeclarationType {
    성적인_콘텐츠("성적인 콘텐츠"),
    혐오_발언_또는_상징("혐오 발언,상징"),
    폭력적_콘텐츠("폭력적 콘텐츠"),
    거짓정보("거짓정보"),
    기타("기타");

    private final String name;
}
