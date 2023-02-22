package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.PostboxMessageAnimalItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostboxMessageAnimalItemRepository extends JpaRepository<PostboxMessageAnimalItem, Long> {
    List<PostboxMessageAnimalItem> findAllByPostboxMessageAnimal(PostboxMessageAnimal postboxMessageAnimal);
}
