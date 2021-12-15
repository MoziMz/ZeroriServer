package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.EmailCheckAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailCheckAuthRepository extends JpaRepository<EmailCheckAuth, String>, EmailCheckAuthRepositorySupport {
    Optional<EmailCheckAuth> findByToken(String token);
}
