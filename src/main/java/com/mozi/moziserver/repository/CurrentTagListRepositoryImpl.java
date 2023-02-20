package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.CurrentTagList;
import com.mozi.moziserver.model.entity.QCurrentTagList;
import com.mozi.moziserver.model.entity.QTag;
import com.mozi.moziserver.model.entity.Tag;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

public class CurrentTagListRepositoryImpl extends QuerydslRepositorySupport implements CurrentTagListRepositorySupport {
    private final QCurrentTagList qCurrentTagList = QCurrentTagList.currentTagList;

    private final QTag qTag = QTag.tag;

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

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    @Override
    public Optional<CurrentTagList> findByTag(Tag tag) {
        return Optional.ofNullable(from(qCurrentTagList)
                .innerJoin(qCurrentTagList.tag, qTag)
                .where(qCurrentTagList.tag.eq(tag))
                .fetchOne());
    }
}
