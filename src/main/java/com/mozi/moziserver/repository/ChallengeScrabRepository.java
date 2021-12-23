package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ChallengeScrab;
import com.mozi.moziserver.model.entity.ChallengeUserSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeScrabRepository extends JpaRepository<ChallengeScrab, ChallengeUserSeq>, ChallengeScrabRepositorySupport{
    @Modifying
    @Query(value = "DELETE FROM challenge_scrab WHERE user_seq = :userSeq AND challenge_seq = :challengeSeq", nativeQuery = true)
    int deleteChallengeScrabByUserSeqAndChallengeSeq(@Param("userSeq") Long userSeq,@Param("challengeSeq") Long challengeSeq);
}
