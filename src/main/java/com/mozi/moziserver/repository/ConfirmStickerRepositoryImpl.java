package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmStickerRepositoryImpl extends QuerydslRepositorySupport implements ConfirmStickerRepositorySupport{

    private final QConfirmSticker qConfirmSticker=QConfirmSticker.confirmSticker;

    public ConfirmStickerRepositoryImpl() {super(ConfirmSticker.class);}

    @Override
    public List<ConfirmSticker> findByUserAndSeqAndDate(Long userSeq, Long seq, Date date){

        List<ConfirmSticker> confirmStickerList=from(qConfirmSticker)
                .where(qConfirmSticker.userSeq.in(userSeq),qConfirmSticker.challengeSeq.in(seq),qConfirmSticker.date.in(date))
                .fetch()
                .stream()
                .collect(Collectors.toList());

        return confirmStickerList;

    }
}
