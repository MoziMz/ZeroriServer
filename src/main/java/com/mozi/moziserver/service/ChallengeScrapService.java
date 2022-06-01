package com.mozi.moziserver.service;

import com.mozi.moziserver.model.entity.ChallengeScrap;
import com.mozi.moziserver.repository.ChallengeScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeScrapService {
    final ChallengeScrapRepository challengeScrapRepository;

    @Transactional
    public List<ChallengeScrap> getChallengeScrapList(Long userSeq){
        return challengeScrapRepository.findByUserSeq(userSeq);
    }


}
