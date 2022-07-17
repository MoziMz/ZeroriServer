package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.PreparationItem;
import com.mozi.moziserver.model.entity.QPreparationItem;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class PreparationItemRepositoryImpl extends QuerydslRepositorySupport implements PreparationItemRepositorySupport{
    private final QPreparationItem qPreparationItem=QPreparationItem.preparationItem;

    public PreparationItemRepositoryImpl() { super(PreparationItem.class);}

    @Override
    public PreparationItem findByAnimalSeqAndTurn(Long animalSeq,Integer turn){
        return from(qPreparationItem)
                .select(qPreparationItem)
                .where(qPreparationItem.animalSeq.eq(animalSeq).and(qPreparationItem.turn.eq(turn)))
                .fetchOne();
    }
}
