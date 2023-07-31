package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.CurrentTopicList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrentTopicRepository extends JpaRepository<CurrentTopicList, Long> {

    List<CurrentTopicList> findByOrderByTurnAsc();
}
