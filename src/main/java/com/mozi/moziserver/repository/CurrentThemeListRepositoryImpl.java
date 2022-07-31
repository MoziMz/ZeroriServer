package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.CurrentThemeList;
import com.mozi.moziserver.model.entity.QCurrentThemeList;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class CurrentThemeListRepositoryImpl extends QuerydslRepositorySupport implements CurrentThemeListRepositorySupport {
    private final QCurrentThemeList qCurrentThemeList = QCurrentThemeList.currentThemeList;

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
}
