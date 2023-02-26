package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.Island;

import java.util.List;

public interface AnimalRepositorySupprot {
    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<Animal> findAllByIsland(Island island, Integer pageNumber, Integer pageSize);
}
