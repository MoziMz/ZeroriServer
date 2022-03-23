package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.req.ReqAdminChallengeCreate;
import com.mozi.moziserver.model.req.ReqChallengeList;
import com.mozi.moziserver.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final ChallengeScrapRepository challengeScrapRepository;
    private final ChallengeRecordRepository challengeRecordRepository;

    // 챌린지 하나 조회
    public Challenge getChallenge(Long seq) {
        return challengeRepository.findBySeq(seq)
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


    @Transactional
    public ChallengeScrap getChallengeScrap(Long challengeSeq,Long userSeq){
        return challengeScrapRepository.findByChallengeSeqAndUserSeq(challengeSeq,userSeq);

    }

    //챌린지 스크랩 생성
    @Transactional
    public void createChallengeScrap(Long userSeq, Long seq){

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
    public void deleteChallengeScrap(Long userSeq, Long seq){
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

//     챌린지 생성
    @Transactional
    public void createChallenge(ReqAdminChallengeCreate req) {
        final Challenge challenge = Challenge.builder()
                .name(req.getName())
                .description(req.getDescription())
                .recommendedCnt(req.getRecommendedCnt())
                .mainTag(req.getMainTag())
                .themeSeq(req.getThemeSeq())
                .point(req.getPoint())
                .build();

        try{
            challengeRepository.save(challenge);

            // TODO 챌린지 태그 리스트 저장

            final ChallengeRecord challengeRecord = ChallengeRecord.builder()
                    .challenge(challenge)
                    .build();

            challengeRecordRepository.save(challengeRecord);

        }catch (Exception e){
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();
        }
    }

}
