package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.ChallengeStatistics;

import java.util.List;

public interface ChallengeStatisticsRepositorySupport {

    List<ChallengeStatistics> findAllByPeriod(
            Long challengeSeq,
            Integer startYear,
            Integer startMonth,
            Integer endYear,
            Integer endMonth
    );
}
