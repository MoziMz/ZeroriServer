package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum QuestionCategory {
    ERROR_QUESTION(1, "오류신고"),
    FEEDBACK_QUESTION(2, "자유로운 피드백"),
    CORRECTION_OF_INFORMATION_QUESTION(3, "정보 정정");

    private final int type;
    private final String name;

    public static QuestionCategory valueOf(int type) {
        for (QuestionCategory QuestionCategory : values())
            if (QuestionCategory.getType() == type)
                return QuestionCategory;

        return null;
    }
}
