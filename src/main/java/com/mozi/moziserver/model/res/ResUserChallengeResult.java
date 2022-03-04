package com.mozi.moziserver.model.res;
import com.mozi.moziserver.model.UserChallengeResult;
import com.mozi.moziserver.model.mappedenum.UserChallengeResultType;
import lombok.Getter;

@Getter
public class ResUserChallengeResult {
    private Integer turn;
    private UserChallengeResultType result;

    private ResUserChallengeResult(UserChallengeResult result){
        this.turn = result.getTurn();
        this.result = result.getResult();
    }

    public static ResUserChallengeResult of(UserChallengeResult result) {
        return new ResUserChallengeResult(result);
    }
}
