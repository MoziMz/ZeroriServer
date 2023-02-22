package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeRepositorySupport {
    @Modifying
    @Query(name = "SELECT FROM challenge WHERE seq IN :challengeSeqList")
    List<Challenge> findAllBySeqIn(@Param("challengeSeqList") List<Long> challengeSeqList);
}
