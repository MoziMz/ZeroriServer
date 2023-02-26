package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.AnimalItem;

import java.util.List;

public interface AnimalItemRepositorySupport {
    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<AnimalItem> findAllByAnimalAndPaging(Animal animal, Integer pageNumber, Integer pageSize);
}
