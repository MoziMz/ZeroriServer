package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostboxMessageAnimalRepository extends JpaRepository<PostboxMessageAnimal, Long>, PostboxMessageAnimalRepositorySupport {
}
