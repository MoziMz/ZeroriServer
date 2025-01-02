package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuth, String> {
    Optional<EmailAuth> findByToken(String token);

    @Query(value = "SELECT * FROM email_auth WHERE user_seq = :userSeq And type = :type ORDER BY created_at desc limit 1", nativeQuery = true)
    Optional<EmailAuth> findByUserAndTypeOrderByCreatedAt(@Param("userSeq") Long userSeq, @Param("type") int type);
}
