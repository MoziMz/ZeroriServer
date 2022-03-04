package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ConfirmSticker;

import java.util.List;

public interface ConfirmStickerRepositorySupport {
    List<ConfirmSticker> findAllBySeq(Long seq);

    Boolean findByUserAndConfirmSeq(Long userSeq, Long confirmSeq);

}
