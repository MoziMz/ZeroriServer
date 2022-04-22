package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostboxMessageAnimalRepositoryImpl extends QuerydslRepositorySupport implements PostboxMessageAnimalRepositorySupport {
    private final QPostboxMessageAnimal qPostboxMessageAnimal = QPostboxMessageAnimal.postboxMessageAnimal;
    private final QPreparationItem qPreparationItem = QPreparationItem.preparationItem;
    private final QAnimal qAnimal = QAnimal.animal;
    private final QUser qUser = QUser.user;

    public PostboxMessageAnimalRepositoryImpl() {
        super(PostboxMessageAnimal.class);
    }

    @Override
    public PostboxMessageAnimal findAnimalInfoByUser(User user) {
        return from(qPostboxMessageAnimal)
                .innerJoin(qPostboxMessageAnimal.user, qUser).fetchJoin()
                .innerJoin(qPostboxMessageAnimal.animal, qAnimal).fetchJoin()
                .where(qPostboxMessageAnimal.user.eq(user))
                .where(qPostboxMessageAnimal.animal.seq.eq(
                        JPAExpressions.select(qPostboxMessageAnimal.animal.seq.max())
                                .from(qPostboxMessageAnimal)
                ))
                .orderBy(qPostboxMessageAnimal.level.desc())
                .fetchFirst();
    }

    @Override
    public List<PreparationItem> findItemByUser(User user, Long animalSeq) {
        List<PreparationItem> preparationItemList = from(qPreparationItem)
                .where(qPreparationItem.animalSeq.eq(animalSeq))
                .orderBy(qPreparationItem.turn.asc())
                .fetchAll()
                .fetch()
                .stream()
                .collect(Collectors.toList());

        return preparationItemList;

    }

    @Override
    public List<PostboxMessageAnimal> findAllByUser(User user) {
         return from(qPostboxMessageAnimal)
                 .innerJoin(qPostboxMessageAnimal.user, qUser).fetchJoin()
                 .innerJoin(qPostboxMessageAnimal.animal, qAnimal).fetchJoin()
                 .where(qUser.eq(user))
                 .fetch()
                 .stream()
                 .collect(Collectors.toList());
    }
}
