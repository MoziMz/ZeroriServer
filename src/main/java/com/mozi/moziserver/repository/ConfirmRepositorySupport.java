package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ConfirmRepositorySupport {
    List<Confirm> findAllByOrderDesc();

    List<Confirm> findByChallengeByOrderDesc(Long seq);

    List<Confirm> findByUserByOrderDesc(Long userSeq);

    //Optional<Confirm> findByUserAndChallengeSeqAndDate(Long userSeq,Long seq,Date date);
}
