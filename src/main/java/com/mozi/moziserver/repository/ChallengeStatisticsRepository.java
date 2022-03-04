package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ChallengeStatisticsRepository extends JpaRepository<ChallengeStatistics, Long> {
    Optional<ChallengeStatistics> findByChallengeAndStartDate(Challenge challenge, LocalDate monthFirstDate);


}
