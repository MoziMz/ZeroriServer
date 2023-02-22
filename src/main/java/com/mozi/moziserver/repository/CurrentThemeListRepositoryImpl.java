package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ChallengeTheme;
import com.mozi.moziserver.model.entity.CurrentThemeList;
import com.mozi.moziserver.model.entity.QChallengeTheme;
import com.mozi.moziserver.model.entity.QCurrentThemeList;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

public class CurrentThemeListRepositoryImpl extends QuerydslRepositorySupport implements CurrentThemeListRepositorySupport {
    private final QCurrentThemeList qCurrentThemeList = QCurrentThemeList.currentThemeList;
    private final QChallengeTheme qChallengeTheme = QChallengeTheme.challengeTheme;

    public CurrentThemeListRepositoryImpl() {
        super(CurrentThemeList.class);
    }

    @Override
    public List<CurrentThemeList> findAllByOrderByTurn() {
        List<CurrentThemeList> currentThemeList = from(qCurrentThemeList)
                .orderBy(qCurrentThemeList.turn.asc())
                .fetch();

        return currentThemeList;
    }

    // -------------------- -------------------- below admin methods -------------------- -------------------- //

    @Override
    public Optional<CurrentThemeList> findByChallengeTheme(ChallengeTheme challengeTheme) {
        return Optional.ofNullable(from(qCurrentThemeList)
                .innerJoin(qCurrentThemeList.challengeTheme, qChallengeTheme)
                .where(qCurrentThemeList.challengeTheme.eq(challengeTheme))
                .fetchOne());
    }
}
