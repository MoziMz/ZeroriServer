package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.PostboxMessageAnimalContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostboxMessageAnimalContentRepository extends JpaRepository<PostboxMessageAnimalContent, Long> {
    PostboxMessageAnimalContent findByAnimalSeq(Long animalSeq);
}
