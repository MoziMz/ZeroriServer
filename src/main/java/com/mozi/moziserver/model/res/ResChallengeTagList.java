package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.ChallengeTag;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;

@Getter
public class ResChallengeTagList {
    private ChallengeTagType name;

    private Integer turn;

    private ResChallengeTagList(ChallengeTag challengeTag) {
        this.name = challengeTag.getTag().getName();
        this.turn = challengeTag.getTurn();
    }

    public static ResChallengeTagList of(ChallengeTag challengeTag) {return new ResChallengeTagList(challengeTag);}
}
