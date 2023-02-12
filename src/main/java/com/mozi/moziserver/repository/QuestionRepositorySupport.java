package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Question;
import com.mozi.moziserver.model.mappedenum.PriorityType;
import com.mozi.moziserver.model.mappedenum.QuestionCategoryType;
import com.mozi.moziserver.model.mappedenum.QuestionStateType;

import java.util.List;

public interface QuestionRepositorySupport {
    Long findSeq();

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<Question> findAllByCategoryAndStateAndPriority(QuestionCategoryType category, QuestionStateType state, PriorityType priority, Integer pageNumber, Integer pageSize);
}
