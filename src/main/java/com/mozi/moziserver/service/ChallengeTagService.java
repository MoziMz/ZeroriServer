package com.mozi.moziserver.service;

import com.mozi.moziserver.model.res.ResChallengeTagList;
import com.mozi.moziserver.repository.ChallengeTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeTagService {

    private final ChallengeTagRepository challengeTagRepository;

    // 챌린지 태그 리스트 조회
    public List<ResChallengeTagList> getChallengeTagList(Long seq) {
        return challengeTagRepository.findAllByChallengeSeq(
                seq
        );
    }
}
