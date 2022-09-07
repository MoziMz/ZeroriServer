package com.mozi.moziserver.model.res;

import lombok.Getter;

import java.util.List;

@Getter
public class ResSearchOfChallengeList {
    private long challengeCnt;

    private List<ResChallengeList> resChallengeList;

    private ResSearchOfChallengeList(long challengeCnt,List<ResChallengeList> resChallengeList) {

        this.challengeCnt=challengeCnt;

        this.resChallengeList=resChallengeList;
    }

    public static ResSearchOfChallengeList of(long challengeCnt,List<ResChallengeList> resChallengeList) {return new ResSearchOfChallengeList(challengeCnt,resChallengeList);}

}
