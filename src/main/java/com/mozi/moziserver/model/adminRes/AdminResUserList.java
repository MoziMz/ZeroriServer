package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.common.UserState;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.entity.UserReward;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import lombok.Getter;

@Getter
public class AdminResUserList {

    private final Long seq;
    private final String email;
    private final String nickName;
    private final UserAuthType type;
    private final UserState state;
    private final Integer point;

    private AdminResUserList(UserAuth userAuth, User user, UserReward userReward) {
        this.seq = user.getSeq();
        this.email = user.getEmail();
        this.nickName = user.getNickName();
        this.type = userAuth.getType();
        this.state = user.getState();
        this.point = userReward.getPoint();
    }

    public static AdminResUserList of(UserAuth userAuth, User user, UserReward userReward) {
        return new AdminResUserList(userAuth, user, userReward);
    }
}
