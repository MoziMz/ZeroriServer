package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ConfirmRepository extends JpaRepository<Confirm, ConfirmId>, ConfirmRepositorySupport{
    @Modifying
    @Query(value = "DELETE FROM confirm WHERE user_seq = :userSeq AND challenge_seq = :challengeSeq AND date = :date", nativeQuery = true)
    int deleteConfirm(@Param("userSeq") Long userSeq,@Param("challengeSeq") Long challengeSeq,@Param("date") Date date);
}
