package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.UserPointRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRecordRepository extends JpaRepository<UserPointRecord,Long>,UserPointRecordRepositorySupport {
}
