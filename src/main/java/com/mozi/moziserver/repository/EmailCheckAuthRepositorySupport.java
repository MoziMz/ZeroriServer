package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.EmailCheckAuth;
import com.mozi.moziserver.model.entity.User;

public interface EmailCheckAuthRepositorySupport {
    EmailCheckAuth getUserEmail(User user);
}
