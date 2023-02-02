package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.IslandImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IslandImgRepository extends JpaRepository<IslandImg, Integer> ,IslandImgRepositorySupport{
    public List<IslandImg> findAllByType(Integer type);

    @Transactional
    public void deleteByType(Integer type);
}
