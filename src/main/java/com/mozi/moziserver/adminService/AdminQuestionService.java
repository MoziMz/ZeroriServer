package com.mozi.moziserver.adminService;

import com.mozi.moziserver.model.entity.Question;
import com.mozi.moziserver.model.mappedenum.PriorityType;
import com.mozi.moziserver.model.mappedenum.QuestionCategoryType;
import com.mozi.moziserver.model.mappedenum.QuestionStateType;
import com.mozi.moziserver.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminQuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getQuestionList(QuestionCategoryType category, QuestionStateType state, PriorityType priorityType, Integer pageNumber, Integer pageSize) {
        return questionRepository.findAllByCategoryAndStateAndPriority(category, state, priorityType, pageNumber, pageSize);
    }
}
