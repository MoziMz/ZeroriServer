package com.mozi.moziserver.adminService;

import com.mozi.moziserver.httpException.ResponseError;
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

    public Question getById(Long seq) {
        return questionRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.QUESTION_NOT_EXISTS::getResponseException);
    }

    public List<Question> getQuestionList(QuestionCategoryType category, QuestionStateType state, PriorityType priorityType, Integer pageNumber, Integer pageSize) {
        return questionRepository.findAllByCategoryAndStateAndPriority(category, state, priorityType, pageNumber, pageSize);
    }

    public void updateStateAndPriority(Long seq, QuestionStateType state, PriorityType priority) {
        Question question = getById(seq);

        if (state != null) {
            question.setState(state);
        }

        if (priority != null) {
            question.setPriority(priority);
        }

        questionRepository.save(question);
    }

    public void deleteQuestion(Long seq) {
        Question question = getById(seq);

        questionRepository.delete(question);
    }
}
