package com.mozi.moziserver.adminService;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Suggestion;
import com.mozi.moziserver.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminSuggestionService {

    private final SuggestionRepository suggestionRepository;

    public Suggestion getById(Long seq) {
        return suggestionRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.SUGGESTION_NOT_EXISTS::getResponseException);
    }

    public List<Suggestion> getSuggestionList(Integer pageNumber, Integer pageSize) {
        return suggestionRepository.findAll(pageNumber, pageSize);
    }

    public void deleteSuggestion(Long seq) {
        Suggestion suggestion = getById(seq);

        suggestionRepository.delete(suggestion);
    }
}
