package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionCategoryType {
    ERROR_QUESTION(1, "오류신고"),
    FEEDBACK_QUESTION(2, "자유로운 피드백"),
    CORRECTION_OF_INFORMATION_QUESTION(3, "정보 정정");

    @Getter
    private final int type;
    @Getter
    private final String name;

    public static QuestionCategoryType valueOf(int type) {
        for (QuestionCategoryType questionCategory : values())
            if (questionCategory.getType() == type)
                return questionCategory;

        return null;
    }
}
