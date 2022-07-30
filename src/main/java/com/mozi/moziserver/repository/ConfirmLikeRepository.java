package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfirmLikeRepository extends JpaRepository<ConfirmLike, Long>, ConfirmLikeRepositorySupport{
    void deleteByConfirmSeqAndUserSeq(Long confirmSeq, Long userSeq);
}
