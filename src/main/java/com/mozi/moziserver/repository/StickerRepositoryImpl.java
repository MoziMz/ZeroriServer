package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QSticker;
import com.mozi.moziserver.model.entity.Sticker;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class StickerRepositoryImpl extends QuerydslRepositorySupport implements StickerRepositorySupport {
    private final QSticker qSticker = QSticker.sticker;

    public StickerRepositoryImpl() {
        super(Sticker.class);
    }

    @Override
    public List<Sticker> findAllBySeq(List<Long> stickerSeqList){
        BooleanBuilder builder = new BooleanBuilder();
        for(Long seq:stickerSeqList){
            builder.or(qSticker.seq.eq(seq));
        }



        List<Sticker> stickerList=from(qSticker)
                .where(builder)
                .fetch();

        return stickerList;

    }
}
