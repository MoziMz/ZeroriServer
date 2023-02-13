package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Suggestion;

import java.util.List;

public interface SuggestionRepositorySupport {
    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<Suggestion> findAll(Integer pageNumber, Integer pageSize);
}
