package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserNotice;
import com.mozi.moziserver.model.mappedenum.UserNoticeType;

import java.util.Optional;

public interface UserNoticeRepositorySupport {
    Optional<UserNotice> findOneByUserAndTypeAndCheckedState(User user, UserNoticeType type);
}
