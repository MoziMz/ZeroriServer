package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.QUserChallenge;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Collection;
import java.util.Optional;

public class UserChallengeRepositoryImpl extends QuerydslRepositorySupport implements UserChallengeRepositorySupport {
    private final QUserChallenge qUserChallenge = QUserChallenge.userChallenge;

    public UserChallengeRepositoryImpl() { super(UserChallenge.class); }

    @Override
    public Optional<UserChallenge> findUserChallengeByUserSeqAndChallengeAndStates(Long userSeq, Challenge challenge, Collection<UserChallengeStateType> states){

        return from(qUserChallenge)
                .where(qUserChallenge.user.seq.eq(userSeq)
                    .and(qUserChallenge.challenge.eq(challenge))
                    .and(qUserChallenge.state.in(states)))
                .fetch()
                .stream()
                .findFirst();
    }

}
