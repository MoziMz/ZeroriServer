package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.DeclarationType;
import com.mozi.moziserver.model.req.ReqConfirm;
import com.mozi.moziserver.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmService {
    private final UserRepository userRepository;

    private final ChallengeRepository challengeRepository;

    private final ConfirmRepository confirmRepository;

    private final DeclarationRepository declarationRepository;

    private final ConfirmStickerRepository confirmStickerRepository;

    //인증 생성
    @Transactional
    public void createConfirm(Long userSeq, Long seq, ReqConfirm reqConfirm){

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Challenge challenge=challengeRepository.findById(seq)
                .orElseThrow(ResponseError.BadRequest.INVALID_SEQ::getResponseException);

        ConfirmId id=new ConfirmId();
        id.setUser(user);
        id.setChallenge(challenge);
        id.setDate(reqConfirm.getDate());

        Confirm confirm=Confirm.builder()
                .id(id)
                .imgUrl(reqConfirm.getImgUrl())
                .build();

        try {
            confirmRepository.save(confirm);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }

    }

    // 인증 모두 조회
    @Transactional
    public List<Confirm> getAllConfirmList() {
        return confirmRepository.findAllByOrderDesc();
    }

    // 챌린지별 인증 조회
    @Transactional
    public List<Confirm> getConfirmList(Long seq) {
        return confirmRepository.findByChallengeByOrderDesc(seq);
    }

    @Transactional
    public List<Confirm> getUserConfirmList(Long userSeq){
        return confirmRepository.findByUserByOrderDesc(userSeq);
    }

    //인증 하나 조회
    @Transactional
    public Confirm getConfirm(Long userSeq, Long seq, Date date) {
        return confirmRepository.findByUserAndChallengeSeqAndDate(userSeq,seq,date);
    }

    //confirmSticker 조회
    @Transactional
    public List<ConfirmSticker> getConfirmStickerList(Long userSeq,Long seq,Date date){
        return confirmStickerRepository.findByUserAndSeqAndDate(userSeq,seq,date);
    }

    @Transactional
    public void deleteConfirm(Long userSeq, Long seq,Date date) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Challenge challenge = challengeRepository.findById(seq)
                .orElseThrow(ResponseError.BadRequest.INVALID_SEQ::getResponseException);
        try {
            int deleteCount = confirmRepository.deleteConfirm(user.getSeq(), challenge.getSeq(), date);
            if (deleteCount == 0) {
                // 동시성 처리: 지울려고 했는데 못 지웠으면 함수실행을 끝낸다.
                throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException();
            }
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException(); // for duplicate exception
        } // FIXME DuplicateKeyException
    }

    //신고 생성
    @Transactional
    public void createDeclaration(Long userSeq, Long seq, Date date, DeclarationType type){

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Challenge challenge=challengeRepository.findById(seq)
                .orElseThrow(ResponseError.BadRequest.INVALID_SEQ::getResponseException);

        ConfirmId id=new ConfirmId();
        id.setUser(user);
        id.setChallenge(challenge);
        id.setDate(date);

        Declaration declaration=Declaration.builder()
                .id(id)
                .declarationType(type)
                .build();

        try {
            declarationRepository.save(declaration);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }

    }
}
