package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ChallengeTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeTopicRepository extends JpaRepository<ChallengeTopic, Long> {
}
