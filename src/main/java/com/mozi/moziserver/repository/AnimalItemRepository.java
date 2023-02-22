package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.AnimalItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimalItemRepository extends JpaRepository<AnimalItem, Long> {
    List<AnimalItem> findAllByAnimal(Animal animal);

    Optional<AnimalItem> findByAnimalAndTurn(Animal animal, Integer turn);

    int countByAnimal(Animal animal);
}
