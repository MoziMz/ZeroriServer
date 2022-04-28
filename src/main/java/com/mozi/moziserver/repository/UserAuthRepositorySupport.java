package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;

public interface UserAuthRepositorySupport {

    User findUserSeqByEmail(String email);

    UserAuth findByUser(User user);
}
