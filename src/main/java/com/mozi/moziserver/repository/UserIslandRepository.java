package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.UserIsland;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserIslandRepository extends JpaRepository<UserIsland, Long>, UserIslandRepositorySupport {
//    @Transactional
//    @Modifying
//    @Query("UPDATE user_island SET rewardLevel = rewardLevel + 1 WHERE user.seq = :userSeq AND island.type = :islandType")
//    void updateUserIslandRewardLevel(Long userSeq, Integer islandType);
}
