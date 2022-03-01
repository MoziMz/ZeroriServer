package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmSticker;

import java.util.List;

public interface ConfirmRepositorySupport {
    List<Confirm> findAllByOrderDesc();

    List<Confirm> findByChallengeByOrderDesc(Long seq,Long prevLastConfirmSeq, Integer pageSize);

    List<Confirm> findByUserByOrderDesc(Long userSeq,Long prevLastConfirmSeq, Integer pageSize);

    Confirm findByUserAndSeq(Long userSeq,Long ConfirmSeq);

    void updateDeclarationState(Confirm confirm,Byte state);

//    List<Confirm> findAllList(Long userSeq,Long challengeSeq,Date date, Integer pageSize);

    List<Confirm> findAllList(Long prevLastConfirmSeq, Integer pageSize);

}
