package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserNotice;
import com.mozi.moziserver.model.mappedenum.UserNoticeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserNoticeRepository extends JpaRepository<UserNotice, Long>, UserNoticeRepositorySupport {

    Optional<UserNotice> findByUserAndType(User user, UserNoticeType type);
}
