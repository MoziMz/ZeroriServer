package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepositorySupport {
    List<User> findAllActiveUserByUserReward(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );
}
