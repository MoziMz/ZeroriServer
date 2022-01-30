package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.req.ReqAdminChallenge;
import com.mozi.moziserver.model.req.ReqChallengeList;
import com.mozi.moziserver.repository.ChallengeRepository;
import com.mozi.moziserver.repository.ChallengeScrapRepository;
import com.mozi.moziserver.repository.ChallengeTagRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final ChallengeScrapRepository challengeScrapRepository;
    private final ChallengeTagRepository challengeTagRepository;

    // 챌린지 하나 조회
    public Challenge getChallenge(Long seq) {
        return challengeRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.USER_CHALLENGE_NOT_EXISTS::getResponseException);
    }

    // 챌린지 모두 조회
    public List<Challenge> getChallengeList(Long userSeq, ReqChallengeList req) {

        return challengeRepository.findAll(
                userSeq,
                req.getChallengeTagType(),
                req.getChallengeThemeType(),
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

        ChallengeScrap challengeScrap = ChallengeScrap.builder()
                .challengeSeq(challenge.getSeq())
                .userSeq(user.getSeq())
                .build();

        try {
            challengeScrapRepository.save(challengeScrap);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        } // FIXME DuplicateKeyException

    }

    @Transactional
    public void deleteChallengeScrab(Long userSeq, Long seq){
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Challenge challenge=challengeRepository.findById(seq)
                .orElseThrow(ResponseError.BadRequest.INVALID_SEQ::getResponseException);
        try {
            int deleteCount = challengeScrapRepository.deleteChallengeScrapByUserSeqAndChallengeSeq(user.getSeq(),challenge.getSeq());
            if (deleteCount == 0) {
                // 동시성 처리: 지울려고 했는데 못 지웠으면 함수실행을 끝낸다.
                throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException();
            }
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException(); // for duplicate exception
        } // FIXME DuplicateKeyException



    }

    // 챌린지 생성
    @Transactional
    public void createChallenge(ReqAdminChallenge req){
        Challenge challenge = new Challenge();

        challenge.setName(req.getName());
        challenge.setDescription(req.getDescription());
        challenge.setRecommendedCnt(req.getRecommendedCnt());
        challenge.setTags(req.getTags());
        challenge.setCurrentPlayerCnt(req.getCurrentPlayerCnt());
        challenge.setTotalPlayerCnt(req.getTotalPlayerCnt());
        challenge.setRepeatPlayerCnt(req.getRepeatPlayerCnt());
        challenge.setTotalCnt(req.getTotalCnt());
        challenge.setTotalChallengeConfirmCnt(req.getTotalChallengeConfirmCnt());
        challenge.setRepeatRate(req.getRepeatRate());
        challenge.setPoint(req.getPoint());
        challenge.setDifficulty(req.getDifficulty());

        try{
            challengeRepository.save(challenge);

            ChallengeTagId challengeTagId = new ChallengeTagId();
            ChallengeTag challengeTag = ChallengeTag.builder().id(challengeTagId).build();

            challengeTag.getId().setChallenge(challenge);
            challengeTag.getId().setTagName(req.getTags());

            challengeTagRepository.save(challengeTag);

        }catch (Exception e){
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();
        }
    }
}
