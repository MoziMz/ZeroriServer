package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.UserPointRecord;
import com.mozi.moziserver.model.mappedenum.PointReasonType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminResUserPointRecordList {

    private final Long seq;
    private final Integer point;
    private final PointReasonType reasonType;
    private final LocalDateTime createdAt;

    private AdminResUserPointRecordList(UserPointRecord userPointRecord) {
        this.seq = userPointRecord.getSeq();
        this.point = userPointRecord.getPoint();
        this.reasonType = userPointRecord.getReason();
        this.createdAt = userPointRecord.getCreatedAt();
    }

    public static AdminResUserPointRecordList of(UserPointRecord userPointRecord) {
        return new AdminResUserPointRecordList(userPointRecord);
    }
}
