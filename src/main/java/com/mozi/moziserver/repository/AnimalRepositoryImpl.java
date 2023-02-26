package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.model.entity.QAnimal;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class AnimalRepositoryImpl extends QuerydslRepositorySupport implements AnimalRepositorySupprot {
    private final QAnimal qAnimal = QAnimal.animal;

    public AnimalRepositoryImpl() {
        super(Animal.class);
    }

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    @Override
    public List<Animal> findAllByIsland(Island island, Integer pageNumber, Integer pageSize) {
        return from(qAnimal)
                .where(island != null ? qAnimal.island.eq(island) : null)
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetch();
    }
}
