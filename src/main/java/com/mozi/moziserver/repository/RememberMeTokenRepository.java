package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.RememberMeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface RememberMeTokenRepository extends JpaRepository<RememberMeToken, String> {

    // SELECT * FROM remember_me_token WHERE series = #{series}
    Optional<RememberMeToken> findRememberMeTokenBySeries(@Param("series") String series);

    // SELECT * FROM remember_me_token WHERE series = #{series}
    //List<RememberMeToken> findRememberMeTokensByUser(@Param("user") User user);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM remember_me_token WHERE user_seq = :userSeq", nativeQuery = true)
    int deleteRememberMeTokensByUserSeq(@Param("userSeq") Long userSeq);
}