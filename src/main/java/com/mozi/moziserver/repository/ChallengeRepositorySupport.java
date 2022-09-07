package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeRecord;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import com.mozi.moziserver.model.mappedenum.ChallengeThemeType;
import com.mozi.moziserver.model.req.ReqChallengeList;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepositorySupport {

    Optional<Challenge> findBySeq (Long seq);

    List<Challenge> findAll (
            Long userSeq,
            List<Long> tagSeqList,
            List<Long> themeSeqList,
            String keyword,
            Integer pageSize,
            Long prevLastPostSeq
    );

    long countChallengeList(
            List<Long> tagSeqList,
            List<Long> themeSeqList,
            String keyword
    );
}
