package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.IslandImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IslandImgRepository extends JpaRepository<IslandImg, Integer> ,IslandImgRepositorySupport{
}
