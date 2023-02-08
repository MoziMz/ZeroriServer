package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.EmailAuth;
import com.mozi.moziserver.model.mappedenum.EmailAuthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuth, String> {
    Optional<EmailAuth> findByToken(String token);

    @Query(value = "SELECT * FROM email_auth WHERE id = :id And type = :type ORDER BY created_at desc limit 1", nativeQuery = true)
    Optional<EmailAuth> findByIdAndTypeOrderByCreatedAt(@Param("id")String id,@Param("type") int type);
}
