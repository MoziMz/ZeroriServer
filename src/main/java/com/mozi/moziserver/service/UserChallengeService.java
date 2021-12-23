package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeScrab;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import com.mozi.moziserver.repository.ChallengeRepository;
import com.mozi.moziserver.repository.ChallengeScrabRepository;
import com.mozi.moziserver.repository.UserChallengeRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserChallengeService {
    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;

    public Optional<UserChallenge> getActiveUserChallenge(Long userSeq, Challenge challenge ) {

        return userChallengeRepository.findUserChallengeByUserSeqAndChallengeAndStates(userSeq, challenge, UserChallengeStateType.activeTypes);
//                .orElseThrow(ResponseError.NotFound.USER_CHALLENGE_NOT_EXISTS::getResponseException);

//        return  userChallenge;
    }

}
