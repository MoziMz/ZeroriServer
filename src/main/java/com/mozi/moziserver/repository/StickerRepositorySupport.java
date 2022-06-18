package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Sticker;

import java.util.List;

public interface StickerRepositorySupport {

    List<Sticker> findAllBySeq(List<Long> stickerSeq);
}
