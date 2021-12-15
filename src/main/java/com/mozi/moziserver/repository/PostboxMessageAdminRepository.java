package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.PostboxMessageAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostboxMessageAdminRepository extends JpaRepository<PostboxMessageAdmin, Long>, PostboxMessageAdminRepositorySupport {
}
