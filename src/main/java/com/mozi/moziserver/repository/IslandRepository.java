package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Island;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IslandRepository extends JpaRepository<Island, Long> {
    List<Island> findAllByOrderBySeqAsc();
}
