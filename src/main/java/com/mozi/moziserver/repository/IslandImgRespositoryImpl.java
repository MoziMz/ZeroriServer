package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.IslandImg;
import com.mozi.moziserver.model.entity.QIslandImg;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public class IslandImgRespositoryImpl extends QuerydslRepositorySupport implements IslandImgRepositorySupport{
    private final QIslandImg qIslandImg=QIslandImg.islandImg;

    @PersistenceContext
    private EntityManager entityManager;

    public IslandImgRespositoryImpl() { super(IslandImg.class); }

    @Override
    public IslandImg findByTypeAndLevel(Integer type, Integer level){
        return from(qIslandImg)
                .select(qIslandImg)
                .where(qIslandImg.type.eq(type).
                        and(qIslandImg.level.eq(level)))
                .fetchOne();
    }
}
