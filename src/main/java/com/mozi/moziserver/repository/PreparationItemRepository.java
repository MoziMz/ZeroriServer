package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.PreparationItem;
import com.mozi.moziserver.model.entity.PreparationItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreparationItemRepository extends JpaRepository<PreparationItem, PreparationItemId>,PreparationItemRepositorySupport {
}
