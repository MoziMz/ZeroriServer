package com.mozi.moziserver.repository;

import com.mozi.moziserver.common.UserState;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;

import java.util.List;

public interface UserAuthRepositorySupport {

    User findUserSeqByEmail(String email);

    UserAuth findByUser(User user);

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<UserAuth> findAllByKeywordAndTypeAndState(
            String keyword,
            UserAuthType userAuthType,
            UserState userState,
            Integer pageNumber,
            Integer pageSize
    );
}
