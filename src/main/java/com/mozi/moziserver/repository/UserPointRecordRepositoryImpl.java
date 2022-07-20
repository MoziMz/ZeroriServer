package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.entity.QUserPointRecord;
import com.mozi.moziserver.model.entity.UserPointRecord;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class UserPointRecordRepositoryImpl extends QuerydslRepositorySupport implements UserPointRecordRepositorySupport{
    private final QUserPointRecord qUserPointRecord=QUserPointRecord.userPointRecord;

    public UserPointRecordRepositoryImpl(){super(UserPointRecord.class);}

}
