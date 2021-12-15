package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.CurrentTagList;

import java.util.List;

public interface CurrentTagListRepositorySupport {
    List<CurrentTagList> findAllByOrderByTurn();
}
