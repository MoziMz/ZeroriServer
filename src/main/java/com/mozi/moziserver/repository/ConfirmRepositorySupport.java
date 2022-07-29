package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmSticker;

import java.util.List;
import java.util.Optional;

public interface ConfirmRepositorySupport {

    List<Confirm> findByUserByOrderDesc(Long userSeq,Long prevLastConfirmSeq, Integer pageSize);
    Confirm findBySeq(Long ConfirmSeq);

    void updateDeclarationState(Confirm confirm,Byte state);

    List<Confirm> findAll(Long prevLastConfirmSeq, Integer pageSize);

    List<Confirm> findAllByChallenge(Challenge challenge, Long prevLastConfirmSeq, Integer pageSize);

    Long findSeq();

    Optional<Confirm> findByChallenge(Challenge challenge);

}
