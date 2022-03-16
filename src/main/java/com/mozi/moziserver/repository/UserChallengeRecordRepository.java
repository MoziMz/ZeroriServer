package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.StickerImg;
import com.mozi.moziserver.model.entity.UserChallengeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChallengeRecordRepository extends JpaRepository<UserChallengeRecord, Long>, UserChallengeRecordRepositorySupport{
}
