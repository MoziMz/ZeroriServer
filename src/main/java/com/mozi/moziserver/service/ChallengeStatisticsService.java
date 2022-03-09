package com.mozi.moziserver.service;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeStatistics;
import com.mozi.moziserver.repository.ChallengeStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeStatisticsService {
    private final ChallengeStatisticsRepository challengeStatisticsRepository;

    public List<ChallengeStatistics> getChallengeStatisticsList( Challenge challenge ) {
        LocalDate today = LocalDate.now();

        return getChallengeStatisticsListByPeriod(challenge, today.getYear(), today.getMonthValue(), 5);
    }

    private List<ChallengeStatistics> getChallengeStatisticsListByPeriod(
            Challenge challenge,
            Integer year,
            Integer month,
            Integer count
    ) {
        int startYear = (year * 12 + month - count) / 12;
        int startMonth = (year * 12 + month - count) % 12 + 1;

        List<ChallengeStatistics> curChallengeStatisticsList = challengeStatisticsRepository.findAllByPeriod(
                challenge.getSeq(),
                startYear,
                startMonth,
                year,
                month
        );

        if( curChallengeStatisticsList == null) {
            curChallengeStatisticsList = new ArrayList<ChallengeStatistics>();
        } else {
            curChallengeStatisticsList = curChallengeStatisticsList.stream()
                    .sorted(Comparator.comparing(ChallengeStatistics::getYear)
                            .thenComparing(ChallengeStatistics::getMonth))
                    .collect(Collectors.toList());
        }

        int curYear = startYear;
        int curMonth = startMonth;

        for( int i = 0; i < count; i++ ) {
            // 챌린지 리스트의 길이가 i와 같거나 작으면
            // i 보다 크더라도 curYear 또는 curMonth 가 다르면
            // 리스트에 추가한다.

            if( curChallengeStatisticsList.size() <= i) {
                curChallengeStatisticsList.add(i, ChallengeStatistics.builder()
                        .challenge(challenge)
                        .year(curYear)
                        .month(curMonth)
                        .playerFirstTryingCnt(0)
                        .playerConfirmCnt(0)
                        .build());
            }
            else if ( curChallengeStatisticsList.get(i).getYear() != curYear || curChallengeStatisticsList.get(i).getMonth() != curMonth) {
                curChallengeStatisticsList.add(i, ChallengeStatistics.builder()
                        .challenge(challenge)
                        .year(curYear)
                        .month(curMonth)
                        .playerFirstTryingCnt(0)
                        .playerConfirmCnt(0)
                        .build());
            }

            curMonth = curMonth % 12 + 1;
            curYear = curMonth == 1 ? curYear + 1 :  curYear;
        }

        return curChallengeStatisticsList;
    }


}
