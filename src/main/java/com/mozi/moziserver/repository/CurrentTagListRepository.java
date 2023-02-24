package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.CurrentTagList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrentTagListRepository extends JpaRepository<CurrentTagList, Long>, CurrentTagListRepositorySupport {
    List<CurrentTagList> findAllByOrderByTurn();
}
