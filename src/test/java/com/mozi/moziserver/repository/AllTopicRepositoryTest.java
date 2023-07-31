package com.mozi.moziserver.repository;


import com.mozi.moziserver.model.entity.CurrentTopicList;
import com.mozi.moziserver.model.entity.Topic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AllTopicRepositoryTest {

    @Autowired
    private CurrentTopicRepository currentTopicRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Test
    @DisplayName("Topic 리스트 조회가 CurrentTopic turn 순서대로 조회되는지 확인")
    void testFindAllOrderByCurrentTopicTurn() {

        List<Topic> topicList = topicRepository.findAllOrderByCurrentTopicTurn();

        List<CurrentTopicList> currentTopicListList = currentTopicRepository.findByOrderByTurnAsc();

        Assertions.assertEquals(topicList.size(), currentTopicListList.size());

        for (int i = 0; i < currentTopicListList.size(); i++) {
            Topic topic = topicList.get(i);
            CurrentTopicList currentTopicList = currentTopicListList.get(i);
            Assertions.assertEquals(currentTopicList.getTopic().getSeq(), topic.getSeq());
        }
    }
}