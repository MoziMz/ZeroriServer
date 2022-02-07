package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.UserSticker;

import java.util.List;

public interface UserStickerRepositorySupport {
    List<UserSticker> findByUserSeq(Long userSeq);

    List<Long> stickerSeqfindByUserSeq(Long userSeq);
}
