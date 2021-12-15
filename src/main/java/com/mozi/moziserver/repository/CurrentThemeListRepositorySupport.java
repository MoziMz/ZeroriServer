package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.CurrentThemeList;

import java.util.List;

public interface CurrentThemeListRepositorySupport {
    List<CurrentThemeList> findAllByOrderByTurn();
}
