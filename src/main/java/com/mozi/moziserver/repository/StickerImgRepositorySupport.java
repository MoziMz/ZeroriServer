package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.StickerImg;

import java.util.List;

public interface StickerImgRepositorySupport {
    List<StickerImg> findAll();

    List<StickerImg> findAllBySeq(List<Long> stickerSeq);
}
