package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserChallengeRecord;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRecordRepositorySupport {
    Optional<UserChallengeRecord> findByChallengeAndUser(Long challengeSeq, User user);

    @Transactional
    void updateAcquisitionPoint(
            Long challengeSeq,
            Long userSeq,
            Integer point
    );

    List<UserChallengeRecord> findByUserAndConfirmCnt(Long userSeq, Long prevLastSeq, Integer pageSize);
}
