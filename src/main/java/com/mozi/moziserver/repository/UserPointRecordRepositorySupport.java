package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserPointRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface UserPointRecordRepositorySupport {
    List<UserPointRecord> findByUserAndPeriod(User user, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
