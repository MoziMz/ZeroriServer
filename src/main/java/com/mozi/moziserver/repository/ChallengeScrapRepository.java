package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ChallengeScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ChallengeScrapRepository extends JpaRepository<ChallengeScrap, Long>, ChallengeScrapRepositorySupport {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM challenge_scrap WHERE user_seq = :userSeq AND challenge_seq = :challengeSeq", nativeQuery = true)
    int deleteChallengeScrapByUserSeqAndChallengeSeq(@Param("userSeq") Long userSeq, @Param("challengeSeq") Long challengeSeq);
}
