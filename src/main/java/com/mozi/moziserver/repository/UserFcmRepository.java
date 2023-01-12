package com.mozi.moziserver.repository;

import java.util.List;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserFcm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFcmRepository extends JpaRepository<UserFcm, Long> {
    Optional<UserFcm> findByUser(User user);
    UserFcm findByToken(String token);
}
