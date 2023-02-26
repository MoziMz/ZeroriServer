package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.AnimalItem;
import com.mozi.moziserver.model.entity.QAnimalItem;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class AnimalItemRepositoryImpl extends QuerydslRepositorySupport implements AnimalItemRepositorySupport {
    private final QAnimalItem qAnimalItem = QAnimalItem.animalItem;

    public AnimalItemRepositoryImpl() {
        super(AnimalItem.class);
    }

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    @Override
    public List<AnimalItem> findAllByAnimalAndPaging(Animal animal, Integer pageNumber, Integer pageSize) {
        return from(qAnimalItem)
                .where(animal != null ? qAnimalItem.animal.eq(animal) : null)
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetch();
    }
}
