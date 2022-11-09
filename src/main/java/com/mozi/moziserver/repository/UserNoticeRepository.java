package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.UserNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNoticeRepository extends JpaRepository<UserNotice, Long>, UserNoticeRepositorySupport{

}
