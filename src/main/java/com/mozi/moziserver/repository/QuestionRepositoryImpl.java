package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QQuestion;
import com.mozi.moziserver.model.entity.QUser;
import com.mozi.moziserver.model.entity.Question;
import com.mozi.moziserver.model.mappedenum.PriorityType;
import com.mozi.moziserver.model.mappedenum.QuestionCategoryType;
import com.mozi.moziserver.model.mappedenum.QuestionStateType;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.function.Function;

public class QuestionRepositoryImpl extends QuerydslRepositorySupport implements QuestionRepositorySupport {
    private final QQuestion qQuestion = QQuestion.question;
    private final QUser qUser = QUser.user;

    public QuestionRepositoryImpl() {
        super(Question.class);
    }

    @Override
    public Long findSeq() {
        return from(qQuestion)
                .select(qQuestion.seq)
                .orderBy(qQuestion.seq.desc())
                .fetchFirst();
    }

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    @Override
    public List<Question> findAllByCategoryAndStateAndPriority(QuestionCategoryType category, QuestionStateType state, PriorityType priority, Integer pageNumber, Integer pageSize) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qQuestion.category::eq, category),
                predicateOptional(qQuestion.state::eq, state),
                predicateOptional(qQuestion.priority::eq, priority)
        };

        return from(qQuestion)
                .innerJoin(qQuestion.user, qUser).fetchJoin()
                .where(predicates)
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetch();
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}