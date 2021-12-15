package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.PostboxMessageAdmin;
import com.mozi.moziserver.model.entity.User;

import java.util.List;

public interface PostboxMessageAdminRepositorySupport {
    List<PostboxMessageAdmin> findAllByUser(
            User user,
            Integer pageSize,
            Long prevLastPostSeq
    );
}
