package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Repository
public interface ConfirmRepository extends JpaRepository<Confirm, Long>, ConfirmRepositorySupport{
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM confirm WHERE seq = :confirmSeq", nativeQuery = true)
    int deleteConfirm(@Param("confirmSeq") Long confirmSeq);

    @Transactional
    @Modifying
    @Query(value = "UPDATE confirm SET likeCnt = likeCnt + 1 WHERE seq = :seq")
    void incrementLikeCnt(@Param("seq") Long seq);

    @Transactional
    @Modifying
    @Query(value = "UPDATE confirm SET likeCnt = likeCnt - 1 WHERE seq = :seq")
    void decrementLikeCnt(@Param("seq") Long seq);
}