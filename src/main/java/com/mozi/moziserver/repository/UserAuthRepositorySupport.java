package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.UserAuth;

public interface UserAuthRepositorySupport {
    UserAuth findUserEmailByNickName(String nickName);
}
