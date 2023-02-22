package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.CurrentThemeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrentThemeListRepository extends JpaRepository<CurrentThemeList, Long>, CurrentThemeListRepositorySupport {
    List<CurrentThemeList> findAllByOrderByTurn();
}
