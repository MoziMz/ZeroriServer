package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ChallengeScrab;
import com.mozi.moziserver.model.entity.ChallengeUserSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeScrabRepository extends JpaRepository<ChallengeScrab, ChallengeUserSeq>, ChallengeScrabRepositorySupport{
}
