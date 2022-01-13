package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmSticker;

import java.util.Date;
import java.util.List;

public interface ConfirmStickerRepositorySupport {
    List<ConfirmSticker> findByUserAndSeqAndDate(Long userSeq, Long seq, Date date);

}
