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
            @Param("beforeState") UserChallengeStateType afterState
    );

    List<UserChallenge> findAllByStateAndStartDate(UserChallengeStateType stateType, LocalDate startDate);

//    @Transactional
//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE booking SET status = :status WHERE id = :id AND status = :prevStatus")
//    int updateBookingStatus(
//            @Param("id") Long id,
//            @Param("prevStatus") BookingStatusType prevStatus,
//            @Param("status") BookingStatusType status
//    );
}
