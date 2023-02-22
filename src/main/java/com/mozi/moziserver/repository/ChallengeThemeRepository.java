package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ChallengeTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeThemeRepository extends JpaRepository<ChallengeTheme, Integer> {
    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    Optional<ChallengeTheme> findByName(String name);

    @Query(value = "SELECT * FROM challenge_theme WHERE challenge_theme.name LIKE concat('%',:name,'%')", nativeQuery = true)
    List<ChallengeTheme> findAllByNameContaining(@Param("name") String name);
}
