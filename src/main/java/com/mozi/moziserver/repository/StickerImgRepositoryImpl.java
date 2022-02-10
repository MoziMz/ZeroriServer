package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QStickerImg;
import com.mozi.moziserver.model.entity.StickerImg;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class StickerImgRepositoryImpl extends QuerydslRepositorySupport implements StickerImgRepositorySupport {
    private final QStickerImg qStickerImg = QStickerImg.stickerImg;

    public StickerImgRepositoryImpl() {
        super(StickerImg.class);
    }

    @Override
    public List<StickerImg> findAll() {
        List<StickerImg> stickerImgList = from(qStickerImg)
                .fetch()
                .stream()
                .collect(Collectors.toList());

        return stickerImgList;
    }

    @Override
    public List<StickerImg> findAllBySeq(List<Long> stickerSeqList){
        BooleanBuilder builder = new BooleanBuilder();
        for(Long seq:stickerSeqList){
            builder.or(qStickerImg.seq.eq(seq));
        }



        List<StickerImg> stickerImgList=from(qStickerImg)
                .where(builder)
                .fetch()
                .stream()
                .collect(Collectors.toList());

        return stickerImgList;

    }
}
