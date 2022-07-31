package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.CurrentTagList;
import com.mozi.moziserver.model.entity.QCurrentTagList;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class CurrentTagListRepositoryImpl extends QuerydslRepositorySupport implements CurrentTagListRepositorySupport {
    private final QCurrentTagList qCurrentTagList = QCurrentTagList.currentTagList;

    public CurrentTagListRepositoryImpl() {
        super(CurrentTagList.class);
    }

    @Override
    public List<CurrentTagList> findAllByOrderByTurn() {
        List<CurrentTagList> currentTagList = from(qCurrentTagList)
                .orderBy(qCurrentTagList.turn.asc())
                .fetch();

        return currentTagList;
    }
}
