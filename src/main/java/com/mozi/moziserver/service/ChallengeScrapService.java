package com.mozi.moziserver.service;

import com.mozi.moziserver.model.entity.ChallengeScrap;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.repository.ChallengeScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeScrapService {

    private final ChallengeScrapRepository challengeScrapRepository;

    public List<ChallengeScrap> getChallengeScrapList(User user) {

        return challengeScrapRepository.findByUser(user);
    }
}
