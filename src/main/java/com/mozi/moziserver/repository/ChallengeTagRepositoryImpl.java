package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ChallengeTag;
import com.mozi.moziserver.model.entity.QChallenge;
import com.mozi.moziserver.model.entity.QChallengeTag;
import com.mozi.moziserver.model.entity.QTag;
import com.mozi.moziserver.model.res.ResChallengeTagList;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class ChallengeTagRepositoryImpl extends QuerydslRepositorySupport implements ChallengeTagRepositorySupport {
    private final QChallengeTag qChallengeTag = QChallengeTag.challengeTag;
    private final QTag qTag = QTag.tag;
    private final QChallenge qChallenge = QChallenge.challenge;

    public ChallengeTagRepositoryImpl() {
        super(ChallengeTag.class);
    }

    @Override
    public List<ResChallengeTagList> findAllByChallengeSeq(Long seq) {
        return from(qChallengeTag)
                .innerJoin(qChallenge).on(qChallengeTag.seq.eq(qChallenge.seq))
                .innerJoin(qTag).on(qChallengeTag.seq.eq(qTag.seq))
                .where(qChallenge.seq.eq(seq))
                .fetch()
                .stream()
                .map(ResChallengeTagList::of)
                .collect(Collectors.toList());
    }
}
