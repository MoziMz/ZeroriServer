package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.entity.QUserPointRecord;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserPointRecord;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;

public class UserPointRecordRepositoryImpl extends QuerydslRepositorySupport implements UserPointRecordRepositorySupport {
    private final QUserPointRecord qUserPointRecord = QUserPointRecord.userPointRecord;

    private final QUser qUser = QUser.user;

    public UserPointRecordRepositoryImpl() {
        super(UserPointRecord.class);
    }

    @Override
    public List<UserPointRecord> findByUserAndPeriod(User user, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Predicate[] predicates = new Predicate[]{
                qUser.eq(user),
                qUserPointRecord.updatedAt.goe(startDateTime).and(qUserPointRecord.updatedAt.lt(endDateTime))
        };

        return from(qUserPointRecord)
                .where(predicates)
                .fetch();
    }

}
