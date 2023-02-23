package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeScrap;
import com.mozi.moziserver.model.entity.ChallengeTheme;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.mappedenum.ChallengeStateType;
import com.mozi.moziserver.model.req.ReqChallengeList;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.repository.ChallengeRepository;
import com.mozi.moziserver.repository.ChallengeScrapRepository;
import com.mozi.moziserver.repository.ChallengeThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeScrapRepository challengeScrapRepository;
    private final ChallengeThemeRepository challengeThemeRepository;

    public Challenge getChallenge(Long seq) {

        Challenge challenge = challengeRepository.findBySeq(seq)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_NOT_EXISTS::getResponseException);

        checkChallengeState(challenge);

        return challenge;
    }

    public List<Challenge> getChallengeList(ReqChallengeList req) {

        return challengeRepository.findAll(
                req.getTagSeqList(),
                req.getThemeSeqList(),
                req.getKeyword(),
                req.getPageSize(),
                req.getPrevLastPostSeq()
        );
    }

    public long getChallengeCnt(ReqChallengeList req) {

        return challengeRepository.countChallengeList(
                req.getTagSeqList(),
                req.getThemeSeqList(),
                req.getKeyword()
        );
    }

    public List<Challenge> getScrappedChallengeList(User user, ReqList req) {

        ChallengeScrap prevChallengeScrap = null;
        if (req.getPrevLastSeq() != null) {
            Challenge prevChallenge = challengeRepository.findBySeq(req.getPrevLastSeq())
                    .orElseThrow(ResponseError.BadRequest.INVALID_SEQ::getResponseException);

            prevChallengeScrap = challengeScrapRepository.findByChallengeAndUser(prevChallenge, user);
        }
        Long prevChallengeScrapSeq = prevChallengeScrap != null ? prevChallengeScrap.getSeq() : null;

        return challengeScrapRepository.findByUser(user, prevChallengeScrapSeq, req.getPageSize()).stream()
                .map(ChallengeScrap::getChallenge)
                .collect(Collectors.toList());
    }

    public ChallengeScrap getChallengeScrap(Challenge challenge, User user) {

        return challengeScrapRepository.findByChallengeAndUser(challenge, user);
    }

    //챌린지 스크랩 생성
    @Transactional
    public void createChallengeScrap(User user, Long seq) {

        Challenge challenge = challengeRepository.findById(seq)
                .orElseThrow(ResponseError.BadRequest.INVALID_SEQ::getResponseException);

        checkChallengeState(challenge);

        ChallengeScrap challengeScrap = ChallengeScrap.builder()
                .challenge(challenge)
                .user(user)
                .build();

        try {
            challengeScrapRepository.save(challengeScrap);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();
        } // FIXME DuplicateKeyException
    }

    @Transactional
    public void deleteChallengeScrap(User user, Long seq) {

        Challenge challenge = challengeRepository.findById(seq)
                .orElseThrow(ResponseError.BadRequest.INVALID_SEQ::getResponseException);

        checkChallengeState(challenge);

        try {
            int deleteCount = challengeScrapRepository.deleteChallengeScrapByUserSeqAndChallengeSeq(user.getSeq(), challenge.getSeq());
            if (deleteCount == 0) {
                // 동시성 처리: 지울려고 했는데 못 지웠으면 함수실행을 끝낸다.
                throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException();
            }
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException(); // for duplicate exception
        } // FIXME DuplicateKeyException
    }

    public List<ChallengeTheme> getChallengeThemeList() {

        return challengeThemeRepository.findAll();
    }

    public void checkChallengeState(Challenge challenge){

        if(challenge.getState() == ChallengeStateType.DELETED){
            throw ResponseError.BadRequest.CHALLENGE_STATE_TYPE_IS_DELETED.getResponseException();
        }
    }
}
