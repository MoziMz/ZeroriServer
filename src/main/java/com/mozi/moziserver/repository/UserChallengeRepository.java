package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long>, UserChallengeRepositorySupport {
}
