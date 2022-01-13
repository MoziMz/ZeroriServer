package com.mozi.moziserver.repository;


import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;

import java.util.List;

public interface MyPageRepositorySupport {

    UserAuth getUserInfo(User user);

    User getAllNickName(User user, String nickName);

    User getAllEmail(User user, String email);

    void updateUserEmail(User user, String email);

    void updateUserNickName(User user, String nickName);

    void updateUserPassword(User user, String pw);
}