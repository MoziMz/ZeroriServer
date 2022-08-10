package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ConfirmLikeRepositoryImpl extends QuerydslRepositorySupport implements ConfirmLikeRepositorySupport {
    QConfirmLike qConfirmLike = QConfirmLike.confirmLike;
    QUser qUser = QUser.user;

    public ConfirmLikeRepositoryImpl() { super(ConfirmLike.class); }

    public List<ConfirmLike> findAllByUserAndConfirmsIn(User user, List<Confirm> confirmList) {
        return from(qConfirmLike)
                .innerJoin(qConfirmLike.user, qUser).fetchJoin()
                .where(qConfirmLike.confirm.in(confirmList)
                        .and(qConfirmLike.user.eq(user)))
                .fetch();
    }
}
