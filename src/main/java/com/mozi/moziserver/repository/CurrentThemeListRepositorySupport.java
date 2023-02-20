package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ChallengeTheme;
import com.mozi.moziserver.model.entity.CurrentThemeList;

import java.util.List;
import java.util.Optional;

public interface CurrentThemeListRepositorySupport {
    List<CurrentThemeList> findAllByOrderByTurn();

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    Optional<CurrentThemeList> findByChallengeTheme(ChallengeTheme challengeTheme);
}
