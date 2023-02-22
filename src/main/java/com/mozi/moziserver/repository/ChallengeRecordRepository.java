package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {
    ChallengeRecord findByChallenge(Challenge challenge);
}
