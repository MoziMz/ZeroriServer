package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeTopic;
import com.mozi.moziserver.model.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeTopicRepository extends JpaRepository<ChallengeTopic, Long> {

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<ChallengeTopic> findByChallengeIn(List<Challenge> challengeList);

    Optional<ChallengeTopic> findByTopicAndChallenge(Topic topic, Challenge challenge);

    void deleteByTopic(Topic topic);
}
