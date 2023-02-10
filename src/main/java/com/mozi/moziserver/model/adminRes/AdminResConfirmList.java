package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.mappedenum.ConfirmStateType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminResConfirmList {

    private final Long seq;
    private final AdminResUser userInfo;
    private final String imgUrl;
    private final String challengeName;
    private final ConfirmStateType state;
    private final Integer reportedCnt;
    private final Integer likeCnt;
    private final boolean exposureState;
    private final LocalDateTime createdAt;

    private AdminResConfirmList(Confirm confirm) {
        this.seq = confirm.getSeq();
        this.userInfo = AdminResUser.of(confirm.getUser());
        this.imgUrl = confirm.getImgUrl();
        this.challengeName = confirm.getChallenge().getName();
        this.state = confirm.getState();
        this.reportedCnt = confirm.getReportedCnt();
        this.likeCnt = confirm.getLikeCnt();
        this.createdAt = confirm.getCreatedAt();
        this.exposureState = ConfirmStateType.activeTypes.contains(confirm.getState());
    }

    public static AdminResConfirmList of(Confirm confirm) {
        return new AdminResConfirmList(confirm);
    }
}
