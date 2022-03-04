package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserReward;
import com.mozi.moziserver.model.entity.UserSticker;
import com.mozi.moziserver.model.entity.UserStickerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRewardRepository extends JpaRepository<UserReward, Long> {
    Optional<UserReward> findByUser(User user);
}
