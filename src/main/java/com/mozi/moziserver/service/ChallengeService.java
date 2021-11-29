package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeScrab;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.req.ReqChallengeList;
import com.mozi.moziserver.repository.ChallengeRepository;
import com.mozi.moziserver.repository.ChallengeScrabRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    private final UserRepository userRepository;

    private final ChallengeScrabRepository challengeScrabRepository;


    // 챌린지 하나 조회
    public Optional<Challenge> getChallenge(Long seq) {
        return challengeRepository.findById(seq);
    }

    // 챌린지 모두 조회
    public List<Challenge> getChallengeList(Long userSeq, ReqChallengeList req) {

        return challengeRepository.findAll(
                userSeq,
                req.getChallengeTagType(),
                req.getPageSize(),
                req.getPrevLastPostSeq()
        );
    }

    //챌린지 스크랩 생성
    @Transactional
    public void createChallengeScrab(Long userSeq, Long seq){

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Challenge challenge=challengeRepository.findById(seq)
                .orElseThrow(ResponseError.BadRequest.INVALID_SEQ::getResponseException);


        ChallengeScrab challengeScrab=ChallengeScrab.builder()
                .challengeSeq(challenge.getSeq())
                .userSeq(user.getSeq())
                .build();

        challengeScrabRepository.save(challengeScrab);

    }

}
