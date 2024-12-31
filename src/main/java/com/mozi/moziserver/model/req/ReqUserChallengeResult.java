package com.mozi.moziserver.model.req;
import com.mozi.moziserver.model.UserChallengeResult;
import com.mozi.moziserver.model.mappedenum.UserChallengeResultType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class ReqUserChallengeResult {
    @NotBlank
    private Integer turn;
    @NotBlank
    //@Pattern(regexp = "(http|https)://(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(/|/([\\w#!:.?+=&%@!\\-/]))?")
    private UserChallengeResultType resultType;

    public UserChallengeResult toUserChallengeResult() {
        return UserChallengeResult.builder()
                .turn(turn)
                .result(resultType)
                .build();
    }
}
