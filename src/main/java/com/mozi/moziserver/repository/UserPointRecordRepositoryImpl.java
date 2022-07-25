package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.entity.QUserPointRecord;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserPointRecord;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class UserPointRecordRepositoryImpl extends QuerydslRepositorySupport implements UserPointRecordRepositorySupport{
    private final QUserPointRecord qUserPointRecord=QUserPointRecord.userPointRecord;

    private final QUser qUser=QUser.user;

    public UserPointRecordRepositoryImpl(){super(UserPointRecord.class);}

    @Override
    public List<UserPointRecord> findByUserAndDate(User user, LocalDateTime startDateTime, LocalDateTime endDateTime){

        return from(qUserPointRecord)
                .where(qUser.eq(user)
                        .and(qUserPointRecord.createdAt.between(startDateTime,endDateTime)))
                .fetch()
                .stream()
                .collect(Collectors.toList());
    }

}
