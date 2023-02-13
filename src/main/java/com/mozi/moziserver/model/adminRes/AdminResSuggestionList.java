package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.Suggestion;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminResSuggestionList {

    private final Long seq;
    private final AdminResUser userInfo;
    private final String challengeName;
    private final String explanation;
    private final LocalDateTime createdAt;

    private AdminResSuggestionList(Suggestion suggestion) {
        this.seq = suggestion.getSeq();
        this.userInfo = AdminResUser.of(suggestion.getUser());
        this.challengeName = suggestion.getChallengeName();
        this.explanation = suggestion.getExplanation();
        this.createdAt = suggestion.getCreatedAt();
    }

    public static AdminResSuggestionList of(Suggestion suggestion) {
        return new AdminResSuggestionList(suggestion);
    }
}
