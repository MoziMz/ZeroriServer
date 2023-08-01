package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.CurrentTopicList;
import com.mozi.moziserver.model.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrentTopicListRepository extends JpaRepository<CurrentTopicList, Long> {

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<CurrentTopicList> findByTurnGreaterThan(Integer turn);

    Optional<CurrentTopicList> findByTopic(Topic topic);
}