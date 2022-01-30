package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.entity.QUserAuth;
import com.mozi.moziserver.model.entity.UserAuth;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.stream.Collectors;

public class UserAuthRepositoryImpl extends QuerydslRepositorySupport implements UserAuthRepositorySupport {
    private final QUserAuth qUserAuth = QUserAuth.userAuth;
    private final QUser qUser = QUser.user;

    public UserAuthRepositoryImpl() {
        super(UserAuth.class);
    }

    @Override
    public UserAuth findUserEmailByNickName(String nickName) {

        return from(qUserAuth)
                .innerJoin(qUser).on(qUserAuth.user.seq.eq(qUser.seq))
                .where(qUser.nickName.eq(nickName), qUserAuth.quit.eq("F"))
                .fetchOne();
    }

    @Override
    public String checkQuitUser(String id) {
        return from(qUserAuth)
                .select(qUserAuth.quit)
                .where(qUserAuth.id.eq(id))
                .fetchOne();
    }
}
