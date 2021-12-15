package com.mozi.moziserver.repository;


import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;

import java.util.List;

public interface MyPageRepositorySupport {

    UserAuth getUserInfo(User user);

    User getUserNickName(User user, String nickName);

    void updateUserEmail(User user, String email);

    User getUserEmail(User user, String email);

    void updateUserInfo(User user, String nickName, String pw);
}