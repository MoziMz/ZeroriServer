package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeTag;
import com.mozi.moziserver.model.entity.ChallengeTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeTagRepository extends JpaRepository<ChallengeTag, ChallengeTagId> {
}
