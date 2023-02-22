package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserIsland;

import java.util.List;

public interface UserIslandRepositorySupport {
    UserIsland findTopByUser(User user);

    List<UserIsland> findAllByUserOrderByIsland(User user);

    UserIsland findTopByUserOrderByIsland(User user);

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<UserIsland> findAllByUserIn(List<User> userList);
}
