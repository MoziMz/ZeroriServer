package com.mozi.moziserver.adminService;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.ChallengeExplanation;
import com.mozi.moziserver.model.ChallengeExplanationContent;
import com.mozi.moziserver.model.adminReq.AdminReqChallengeCreate;
import com.mozi.moziserver.model.adminReq.AdminReqChallengeUpdate;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import com.mozi.moziserver.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final ChallengeThemeRepository challengeThemeRepository;
    private final ChallengeStatisticsRepository challengeStatisticsRepository;
    private final TagRepository tagRepository;
    private final ChallengeTagRepository challengeTagRepository;
    private final PlatformTransactionManager transactionManager;

    @Transactional
    public void createChallenge(AdminReqChallengeCreate req) {
        final Challenge challenge = Challenge.builder()
                .name(req.getName())
                .description(req.getDescription())
                .recommendedCnt(req.getRecommendedCnt())
                .mainTag(req.getMainTag())
                .themeSeq(req.getThemeSeq())
                .point(req.getPoint())
                .build();

        try {
            challengeRepository.save(challenge);

            final ChallengeRecord challengeRecord = ChallengeRecord.builder()
                    .challenge(challenge)
                    .build();

            challengeRecordRepository.save(challengeRecord);

        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();
        }
    }

    public void createChallengeExplanation(Long challengeSeq, String title, List<String> contentList) {
        Challenge challenge = challengeRepository.getById(challengeSeq);

        createChallengeTag(challenge);

        List<ChallengeExplanationContent> challengeExplanationContentList = new ArrayList<>();
        for (int i = 0; i < contentList.size(); i++) {
            challengeExplanationContentList.add(
                    ChallengeExplanationContent.builder()
                            .turn(i + 1)
                            .content(contentList.get(i))
                            .build()
            );
        }

        ChallengeExplanation challengeExplanation = ChallengeExplanation.builder()
                .title(title)
                .contents(challengeExplanationContentList)
                .build();

        challenge.setExplanation(challengeExplanation);
        challengeRepository.save(challenge);
    }

    public Challenge getChallenge(Long seq) {
        Challenge challenge = challengeRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_NOT_EXISTS::getResponseException);

        return challenge;
    }

    public ChallengeTheme getChallengeTheme(Long seq) {
        ChallengeTheme challengeTheme = challengeThemeRepository.findById(seq.intValue())
                .orElseThrow(ResponseError.NotFound.CHALLENGE_THEME_NOT_EXISTS::getResponseException);

        return challengeTheme;
    }

    public Tag getTag(ChallengeTagType tagType) {
        Tag tag = tagRepository.findByName(tagType.getName())
                .orElseThrow(ResponseError.NotFound.TAG_NOT_EXISTS::getResponseException);

        return tag;
    }

    public List<ChallengeStatistics> getChallengeStatisticsListByPeriod(
            Challenge challenge,
            Integer startYear,
            Integer startMonth
    ) {

        LocalDate today = LocalDate.now();

        List<ChallengeStatistics> curChallengeStatisticsList = challengeStatisticsRepository.findAllByPeriod(
                challenge.getSeq(),
                startYear,
                startMonth,
                today.getYear(),
                today.getMonthValue()
        );

        if (curChallengeStatisticsList == null) {
            curChallengeStatisticsList = new ArrayList<>();
        }

        int size = curChallengeStatisticsList.size();
        //월이 없는 달은 통계를 0으로 채운다
        for (int i = 1; i <= today.getMonthValue(); i++) {
            boolean exists = false;
            for (int j = 0; j < size; j++) {
                if (curChallengeStatisticsList.get(j).getMonth() == i) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                curChallengeStatisticsList.add(ChallengeStatistics.builder()
                        .challenge(challenge)
                        .year(startYear)
                        .month(i)
                        .playerFirstTryingCnt(0)
                        .playerConfirmCnt(0)
                        .build());
            }
        }
        curChallengeStatisticsList = curChallengeStatisticsList.stream()
                .sorted(Comparator.comparing(ChallengeStatistics::getYear)
                        .thenComparing(ChallengeStatistics::getMonth))
                .collect(Collectors.toList());

        return curChallengeStatisticsList;
    }


    public List<Challenge> getChallengeListByThemeAndTagAndName(Long themeSeq, ChallengeTagType challengeTagType, String keyword, Integer pageNumber, Integer pageSize) {

        if (themeSeq != null) {
            getChallengeTheme(themeSeq);
        }

        Long tagSeq = null;
        if (challengeTagType != null) {
            tagSeq = getTag(challengeTagType).getSeq();
        }


        return challengeRepository.findAllByThemeAndTagAndName(themeSeq, tagSeq, keyword, pageNumber, pageSize);
    }

    public void createChallengeTag(Challenge challenge) {

        Tag tag = getTag(challenge.getMainTag());

        ChallengeTag challengeTag = ChallengeTag.builder()
                .tag(tag)
                .challenge(challenge)
                .turn(1)
                .build();

        challengeTagRepository.save(challengeTag);
    }

    public void updateChallengeTag(Challenge challenge, ChallengeTagType challengeTagType) {


        Tag beforeTag = getTag(challenge.getMainTag());

        Tag newTag = getTag(challengeTagType);

        //challenge와 tag로 찾는다.->유니크는 아니지만 일단은 하나
        ChallengeTag challengeTag = challengeTagRepository.findByChallengeAndTag(challenge, beforeTag)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_TAG_NOT_EXISTS::getResponseException);

        challengeTag.setTag(newTag);

        challengeTagRepository.save(challengeTag);
    }

    @Transactional
    public void updateChallenge(Long seq, AdminReqChallengeUpdate req, String title, List<String> contentList) {
        final Challenge challenge = getChallenge(seq);

        if (req.getName() != null) {
            challenge.setName(req.getName());
        }

        if (req.getDescription() != null) {
            challenge.setDescription(req.getDescription());
        }

        if (req.getRecommendedCnt() != null) {
            challenge.setRecommendedCnt(req.getRecommendedCnt());
        }


        if (req.getMainTag() != null) {

            updateChallengeTag(challenge, req.getMainTag());

            challenge.setMainTag(req.getMainTag());
        }

        if (req.getThemeSeq() != null) {
            challenge.setThemeSeq(req.getThemeSeq());
        }

        if (req.getPoint() != null) {
            challenge.setPoint(req.getPoint());
        }

        if (title != null || contentList != null) {
            ChallengeExplanation challengeExplanation = updateChallengeExplanation(challenge, title, contentList);
            challenge.setExplanation(challengeExplanation);

        }

        challengeRepository.save(challenge);
    }

    public ChallengeExplanation updateChallengeExplanation(Challenge challenge, String title, List<String> contentList) {

        List<ChallengeExplanationContent> challengeExplanationContentList = new ArrayList<>();
        if (contentList != null) {
            for (int i = 0; i < contentList.size(); i++) {
                challengeExplanationContentList.add(
                        ChallengeExplanationContent.builder()
                                .turn(i + 1)
                                .content(contentList.get(i))
                                .build()
                );
            }
        }

        ChallengeExplanation challengeExplanation = challenge.getExplanation();
        if (title != null) {
            challengeExplanation.setTitle(title);
        }

        if (contentList != null) {
            challengeExplanation.setContents(challengeExplanationContentList);
        }

        return challengeExplanation;
    }

    private void withTransaction(Runnable runnable) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();

        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            runnable.run();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
    }
}
