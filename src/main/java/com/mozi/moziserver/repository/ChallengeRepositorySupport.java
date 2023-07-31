package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Challenge;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepositorySupport {

    Optional<Challenge> findBySeq(Long seq);

    List<Challenge> findAll(
            List<Long> tagSeqList,
            List<Long> themeSeqList,
            Long topicSeq,
            String keyword,
            Integer pageSize,
            Long prevLastPostSeq
    );

    long countChallengeList(
            List<Long> tagSeqList,
            List<Long> themeSeqList,
            String keyword
    );

    // -------------------- -------------------- below admin methods -------------------- -------------------- //

    List<Challenge> findAllByThemeAndTagAndName(
            Long themeSeq,
            Long tagSeq,
            String keyword,
            Integer pageNumber,
            Integer pageSize
    );
}
