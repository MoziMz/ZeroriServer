package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long>, UserChallengeRepositorySupport {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE user_challenge SET state = :afterState WHERE seq = :seq AND state = :beforeState")
    int updateUserChallengeState(
            @Param("seq") Long seq,
            @Param("beforeState") UserChallengeStateType beforeState,
            @Param("afterState") UserChallengeStateType afterState
    );

    Optional<UserChallenge> findFirstByStateNotAndUserSeqOrderByStartDateAsc(UserChallengeStateType userChallengeStateType, Long userSeq);
}
