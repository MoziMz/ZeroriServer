package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.CurrentThemeList;
import lombok.Getter;

@Getter
public class ResCurrentThemeList {
    private final String name;

    private ResCurrentThemeList(CurrentThemeList currentThemeList) {
        this.name = currentThemeList.getChallengeTheme().getName();
    }

    public static ResCurrentThemeList of(CurrentThemeList currentThemeList) {
        return new ResCurrentThemeList(currentThemeList);
    }
}
