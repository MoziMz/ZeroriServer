package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.ChallengeScrap;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.repository.ChallengeScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeScrapService {
    final ChallengeScrapRepository challengeScrapRepository;
    final UserService userService;


    public List<ChallengeScrap> getChallengeScrapList(Long userSeq){
        User user = userService.getUserBySeq(userSeq)
                .orElseThrow(ResponseError.InternalServerError.UNEXPECTED_ERROR::getResponseException);

        return challengeScrapRepository.findByUser(user);
    }


}
