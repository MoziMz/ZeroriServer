package com.mozi.moziserver.service;

import com.mozi.moziserver.model.entity.Suggestion;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.req.ReqSuggestionCreate;
import com.mozi.moziserver.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuggestionService {
    private final SuggestionRepository suggestionRepository;

    @Transactional
    public void createSuggestion(User user, ReqSuggestionCreate req) {

        Suggestion suggestion = Suggestion.builder()
                .user(user)
                .challengeName(req.getChallengeName())
                .explanation(req.getExplanation())
                .build();

        suggestionRepository.save(suggestion);
    }
}
