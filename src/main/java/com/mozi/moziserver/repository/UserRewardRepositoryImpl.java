package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.entity.QUserReward;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserReward;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;

public class UserRewardRepositoryImpl extends QuerydslRepositorySupport implements UserRewardRepositorySupport {

    private final QUserReward qUserReward = QUserReward.userReward;
    private final QUser qUser = QUser.user;

    public UserRewardRepositoryImpl() {
        super(UserReward.class);
    }

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    @Override
    public List<UserReward> findAllByKeyword(String keyword, Long numberOfKeyword, Integer pageNumber, Integer pageSize) {

        return from(qUserReward)
                .innerJoin(qUserReward.user, qUser).fetchJoin()
                .where(StringUtils.hasLength(keyword) ?
                        numberOfKeyword != null ?
                                qUser.nickName.like('%' + keyword + '%').or(qUser.seq.eq(numberOfKeyword)) :
                                qUser.nickName.like('%' + keyword + '%') :
                        null)
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetch();
    }

    @Override
    public List<UserReward> findAllByUserIn(List<User> userList) {
        return from(qUserReward)
                .where(qUser.in(userList))
                .fetch();
    }
}
