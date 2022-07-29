package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ConfirmLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmLikeRepository extends JpaRepository<ConfirmLike, Long> {
    void deleteByConfirmSeqAndUserSeq(Long confirmSeq, Long userSeq);
}
