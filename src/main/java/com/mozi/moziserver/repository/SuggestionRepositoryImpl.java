package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QSuggestion;
import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.entity.Suggestion;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class SuggestionRepositoryImpl extends QuerydslRepositorySupport implements SuggestionRepositorySupport {

    private final QSuggestion qSuggestion = QSuggestion.suggestion;
    private final QUser qUser = QUser.user;

    public SuggestionRepositoryImpl() {
        super(Suggestion.class);
    }

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    @Override
    public List<Suggestion> findAll(Integer pageNumber, Integer pageSize) {
        return from(qSuggestion)
                .innerJoin(qSuggestion.user, qUser).fetchJoin()
                .orderBy(qSuggestion.createdAt.desc())
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetch();
    }
}
