package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmSticker;

import java.util.List;

public interface ConfirmRepositorySupport {

    List<Confirm> findByChallengeByOrderDesc(Long seq,Long prevLastConfirmSeq, Integer pageSize);

    List<Confirm> findByUserByOrderDesc(Long userSeq,Long prevLastConfirmSeq, Integer pageSize);

    Confirm findBySeq(Long ConfirmSeq);

    void updateDeclarationState(Confirm confirm,Byte state);

    List<Confirm> findAllList(Long prevLastConfirmSeq, Integer pageSize);

    Long findSeq();

}
