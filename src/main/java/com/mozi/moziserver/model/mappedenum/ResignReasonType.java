package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResignReasonType {
    DIFFICULT_OR_UNHELPFUL("제로 활동이 어렵거나 도움이 되지 않아요"),
    DIFFERENT_INFORMATION("제로 활동 정보가 다른 경우가 있어요"),
    NO_ACTIVITIES_I_WANT_TO_DO("내가 하고 싶은 활동이 없어요"),
    LAGS_AND_SLOW("렉이 걸리고 속도가 느릴 때가 잦아요"),
    MANY_ERRORS("사용 도중 오류가 많아요"),
    OTHER_SERVICES("대체할만 한 다른 서비스가 있어요"),
    SIGN_UP_FOR_ANOTHER_ACCOUNT("다른 계정으로 가입하고싶어요"),
    ETC("기타");

    private final String name;
}
