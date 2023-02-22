package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.CurrentTagList;
import com.mozi.moziserver.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface CurrentTagListRepositorySupport {
    List<CurrentTagList> findAllByOrderByTurn();

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    Optional<CurrentTagList> findByTag(Tag tag);
}
