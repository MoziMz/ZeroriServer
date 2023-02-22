package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRewardRepository extends JpaRepository<UserReward, Long>, UserRewardRepositorySupport {

    UserReward findByUser(User user);

    @Transactional
    @Modifying
    @Query("UPDATE user_reward SET point = point + :point WHERE user.seq = :userSeq")
    int incrementPoint(
            @Param("userSeq") Long userSeq,
            @Param("point") int point
    );

    @Transactional
    @Modifying
    @Query("UPDATE user_reward SET point = point-:point WHERE user.seq = :userSeq")
    int decrementPoint(@Param("userSeq") Long userSeq, @Param("point") int point);
}
