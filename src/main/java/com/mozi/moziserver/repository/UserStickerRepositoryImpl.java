package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.entity.QUserSticker;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserSticker;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserStickerRepositoryImpl extends QuerydslRepositorySupport implements UserStickerRepositorySupport {
    private final QUser qUser = QUser.user;
    private final QUserSticker qUserSticker = QUserSticker.userSticker;

    public UserStickerRepositoryImpl() {
        super(UserSticker.class);
    }

    @Override
    public List<UserSticker> findByUserSeq(Long userSeq) {
        User user = from(qUser)
                .where(qUser.seq.eq(userSeq))
                .fetchOne();

        List<UserSticker> userStickerList = from(qUserSticker)
                .where(qUserSticker.id.user.in(user))
                .fetch();

        return userStickerList;
    }

    @Override
    public List<Long> stickerSeqfindByUserSeq(Long userSeq) {
        User user = from(qUser)
                .where(qUser.seq.eq(userSeq))
                .fetchOne();

        List<Long> userStickerSeqList = from(qUserSticker)
                .select(qUserSticker.id.sticker.seq)
                .where(qUserSticker.id.user.in(user))
                .fetch();

        return userStickerSeqList;
    }

}
