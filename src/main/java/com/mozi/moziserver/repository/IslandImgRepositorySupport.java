package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.IslandImg;

import java.util.Optional;

public interface IslandImgRepositorySupport {
    IslandImg findByTypeAndLevel(Integer type, Integer level);
}
