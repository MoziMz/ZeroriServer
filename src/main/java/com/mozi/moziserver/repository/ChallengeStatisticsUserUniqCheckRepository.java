package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface ChallengeStatisticsUserUniqCheckRepository extends JpaRepository<ChallengeStatisticsUserUniqCheck, Long> {
    Optional<ChallengeStatisticsUserUniqCheck> findByChallengeAndYearAndMonthAndUser(Challenge challenge, Integer year, Integer month, User user);
}
