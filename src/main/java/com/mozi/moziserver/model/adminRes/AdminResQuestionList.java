package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.Question;
import com.mozi.moziserver.model.mappedenum.PriorityType;
import com.mozi.moziserver.model.mappedenum.QuestionCategoryType;
import com.mozi.moziserver.model.mappedenum.QuestionStateType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminResQuestionList {

    private final Long seq;
    private final AdminResUser userInfo;
    private final QuestionCategoryType category;
    private final String title;
    private final String content;
    private final String imgUrl;
    private final QuestionStateType state;
    private final PriorityType priority;
    private final LocalDateTime createdAt;

    private AdminResQuestionList(Question question) {
        this.seq = question.getSeq();
        this.userInfo = AdminResUser.of(question.getUser());
        this.category = question.getCategory();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.imgUrl = question.getImgUrl();
        this.state = question.getState();
        this.priority = question.getPriority();
        this.createdAt = question.getCreatedAt();
    }

    public static AdminResQuestionList of(Question question) {
        return new AdminResQuestionList(question);
    }
}
