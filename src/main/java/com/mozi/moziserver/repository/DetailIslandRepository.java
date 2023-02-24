package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.DetailIsland;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailIslandRepository extends JpaRepository<DetailIsland, Long> {

    Optional<DetailIsland> findByIslandSeqAndAnimalTurnAndItemTurn(Long IslandSeq, Integer animalTurn, Integer ItemTurn);
//    @Transactional
//    public void deleteBySeq(Integer type);
}
