package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.AnimalMention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AnimalMentionRepository extends JpaRepository<AnimalMention, Long> {

    @Query("SELECT a FROM animal_mention a WHERE a.animal.seq = :animalSeq AND a.itemTurn = :itemTurn AND a.startPoint <= :userWeekPoint AND :userWeekPoint < (a.startPoint + a.rangePoint)")
    List<AnimalMention> findByAnimalSeqAndItemTurnAndUserWeekPoint(
            @Param("animalSeq") Long animalSeq,
            @Param("itemTurn") Integer itemTurn,
            @Param("userWeekPoint") Integer userWeekPoint
    );
}