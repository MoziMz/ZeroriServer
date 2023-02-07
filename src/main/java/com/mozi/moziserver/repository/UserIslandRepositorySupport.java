package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserIsland;

import java.util.List;

public interface UserIslandRepositorySupport {
    List<UserIsland> findAllByUserOrderByType(User user);
    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<UserIsland> findAllByUserIn(List<User> userList);
}
