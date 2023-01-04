package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Suggestion;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.req.ReqSuggestionCreate;
import com.mozi.moziserver.repository.SuggestionRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuggestionService {
    private final UserRepository userRepository;
    private final SuggestionRepository suggestionRepository;

    @Transactional
    public void createSuggestion(Long userSeq, ReqSuggestionCreate req) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Suggestion suggestion = Suggestion.builder()
                .user(user)
                .challengeName(req.getChallengeName())
                .explanation(req.getExplanation())
                .build();

        suggestionRepository.save(suggestion);
    }
}
