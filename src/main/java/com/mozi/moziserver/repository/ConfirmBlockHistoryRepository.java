package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ConfirmBlockHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmBlockHistoryRepository extends JpaRepository<ConfirmBlockHistory, Long> {
}
