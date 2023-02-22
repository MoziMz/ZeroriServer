package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.User;

import java.util.List;

public interface PostboxMessageAnimalRepositorySupport {
    PostboxMessageAnimal findAnimalInfoByUser(User user);

    List<PostboxMessageAnimal> findAllByUser(User user, Integer pageSize, Long prevLastSeq);

    PostboxMessageAnimal findLastOneByUser(User user);
}
