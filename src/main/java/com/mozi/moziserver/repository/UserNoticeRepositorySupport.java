package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserNotice;

import java.util.List;
import java.util.Optional;

public interface UserNoticeRepositorySupport {
    Optional<UserNotice> findOneByUserAndType(User user, Integer type);
}
