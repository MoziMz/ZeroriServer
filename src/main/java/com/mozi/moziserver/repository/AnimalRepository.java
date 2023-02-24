package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByIslandSeqAndTurn(Long islandSeq, Integer turn);

    int countByIslandSeq(Long islandSeq);
}
