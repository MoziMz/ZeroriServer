package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeStatisticsUserUniqCheck;
import com.mozi.moziserver.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeStatisticsUserUniqCheckRepository extends JpaRepository<ChallengeStatisticsUserUniqCheck, Long> {
    Optional<ChallengeStatisticsUserUniqCheck> findByChallengeAndYearAndMonthAndUser(Challenge challenge, Integer year, Integer month, User user);
}
