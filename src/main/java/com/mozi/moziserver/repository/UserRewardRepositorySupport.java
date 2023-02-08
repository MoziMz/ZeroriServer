package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.UserReward;

import java.util.List;

public interface UserRewardRepositorySupport {

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<UserReward> findAllByKeyword(String keyword, Long numberOfKeyword, Integer pageNumber, Integer pageSize);
}
