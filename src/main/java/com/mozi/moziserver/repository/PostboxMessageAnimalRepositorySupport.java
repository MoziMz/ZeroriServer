package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.PreparationItem;
import com.mozi.moziserver.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface PostboxMessageAnimalRepositorySupport {
    PostboxMessageAnimal findAnimalInfoByUser(User user);

    List<PreparationItem> findItemByUser(User user, Long animalSeq);

    List<PostboxMessageAnimal> findAllByUser(User user, Integer pageSize, Long prevLastSeq);
}
