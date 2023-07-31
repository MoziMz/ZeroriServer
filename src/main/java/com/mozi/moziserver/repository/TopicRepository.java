package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("SELECT tp FROM topic tp INNER JOIN current_topic_list ctl ON tp = ctl.topic ORDER BY ctl.turn")
    List<Topic> findAllOrderByCurrentTopicTurn();
}
