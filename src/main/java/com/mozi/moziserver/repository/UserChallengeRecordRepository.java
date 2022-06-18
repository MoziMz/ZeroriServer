package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Sticker;
import com.mozi.moziserver.model.entity.UserChallengeRecord;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserChallengeRecordRepository extends JpaRepository<UserChallengeRecord, Long>, UserChallengeRecordRepositorySupport{
}
