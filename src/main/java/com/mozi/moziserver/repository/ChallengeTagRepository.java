package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeTag;
import com.mozi.moziserver.model.entity.ChallengeTagId;
import com.mozi.moziserver.model.res.ResChallengeTagList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeTagRepository extends JpaRepository<ChallengeTag, Long>, ChallengeTagRepositorySupport{
    List<ResChallengeTagList> findAllByChallengeSeq(Long seq);
}
