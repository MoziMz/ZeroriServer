package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.CurrentThemeList;
import lombok.Getter;

@Getter
public class AdminResCurrentThemeList {
    private final Long seq;
    private final Integer turn;
    private final Integer challengeThemeSeq;

    private AdminResCurrentThemeList(CurrentThemeList currentThemeList) {
        this.seq = currentThemeList.getSeq();
        this.turn = currentThemeList.getTurn();
        this.challengeThemeSeq = currentThemeList.getChallengeTheme().getSeq();
    }

    public static AdminResCurrentThemeList of(CurrentThemeList currentThemeList) {
        return new AdminResCurrentThemeList(currentThemeList);
    }
}
