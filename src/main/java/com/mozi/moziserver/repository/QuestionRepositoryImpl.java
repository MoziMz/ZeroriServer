package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.QQuestion;
import com.mozi.moziserver.model.entity.Question;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class QuestionRepositoryImpl extends QuerydslRepositorySupport implements QuestionRepositorySupport {
    private final QQuestion qQuestion = QQuestion.question;

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
}
