package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.PreparationItem;

public interface PreparationItemRepositorySupport {
    PreparationItem findByAnimalSeqAndTurn(Long animalSeq,Integer turn);
}
