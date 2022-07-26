package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Animal;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Animal findByIslandTypeAndIslandLevel(Integer islandType, Integer islandLevel);
}
