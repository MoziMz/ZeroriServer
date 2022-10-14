package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.PreparationItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreparationItemRepository extends JpaRepository<PreparationItem, Long>,PreparationItemRepositorySupport {
}
