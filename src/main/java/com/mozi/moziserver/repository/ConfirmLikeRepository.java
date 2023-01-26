package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ConfirmLikeRepository extends JpaRepository<ConfirmLike, Long>, ConfirmLikeRepositorySupport {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM confirm_like WHERE confirm.seq = :confirmSeq AND user.seq = :userSeq")
    int deleteByConfirmSeqAndUserSeq(@Param("confirmSeq") Long confirmSeq, @Param("userSeq") Long userSeq);
}
