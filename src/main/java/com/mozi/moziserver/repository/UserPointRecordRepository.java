package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserPointRecord;
import com.mozi.moziserver.model.mappedenum.PointReasonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserPointRecordRepository extends JpaRepository<UserPointRecord,Long>,UserPointRecordRepositorySupport {

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<UserPointRecord> findAllByUserAndReasonIn(User user, List<PointReasonType> pointReasonTypeList);
    List<UserPointRecord> findAllByUser(User user);
}