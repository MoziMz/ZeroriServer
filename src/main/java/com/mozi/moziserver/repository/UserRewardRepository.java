package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserReward;
import com.mozi.moziserver.model.entity.UserSticker;
import com.mozi.moziserver.model.entity.UserStickerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRewardRepository extends JpaRepository<UserReward, Long> {
    UserReward findByUser(User user); // TODO 유저 생성시 UserReward 동시 생성?

//    @Transactional
//    @Modifying
//    @Query("UPDATE user_reward SET likeCnt = likeCnt - 1 WHERE seq = :seq")
//    int decrementLikeCnt(@Param("seq") Long seq);

    @Transactional
    @Modifying
    @Query("UPDATE user_reward SET point = point + :point WHERE user_seq = :userSeq")
    int incrementPoint(
            @Param("userSeq") Long userSeq,
            @Param("point") int point
    );
}
