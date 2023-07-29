package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.AnimalMention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimalMentionRepository extends JpaRepository<AnimalMention, Long> {

    @Query(name = "SELECT FROM animal_mention WHERE animal_seq = :animalSeq AND item_turn = :itemTurn AND point >= :point ORDER BY point LIMIT 1")
    Optional<AnimalMention> findByAnimalSeqAndItemTurnAndPoint(
            @Param("animalSeq") Long animalSeq,
            @Param("itemTurn") Integer itemTurn,
            @Param("point") Integer point
    );
}