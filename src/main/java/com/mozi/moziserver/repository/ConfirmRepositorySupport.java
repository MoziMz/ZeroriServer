package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.mappedenum.ConfirmStateType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConfirmRepositorySupport {

    List<Confirm> findByUserByOrderDesc(Long userSeq,Long prevLastConfirmSeq, Integer pageSize);
    Confirm findBySeq(Long ConfirmSeq);

    void updateStateSupportedCnt(Confirm confirm, ConfirmStateType state, Integer cnt);

    List<Confirm> findAll(Long prevLastConfirmSeq, Integer pageSize);

    List<Confirm> findAllByChallenge(Challenge challenge, Long prevLastConfirmSeq, Integer pageSize);

    Optional<Confirm> findByChallenge(Challenge challenge);

    List<Confirm> findByCreatedAt(LocalDateTime localDateTime);

    List<Confirm> findByUserAndPeriod(User user, Challenge challenge, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Confirm> findByPeriodAndPaging(LocalDateTime startDateTime,Long prevLastConfirmSeq, Integer pageSize);


}
