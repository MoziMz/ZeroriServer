package com.mozi.moziserver.adminService;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.ChallengeExplanation;
import com.mozi.moziserver.model.ChallengeExplanationContent;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import com.mozi.moziserver.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final TagRepository tagRepository;
    private final ChallengeTagRepository challengeTagRepository;

    @Transactional
    public void createChallenge(ReqAdminChallengeCreate req) {
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
    public Tag getTag(ChallengeTagType tagType) {
        Tag tag = tagRepository.findByName(tagType.getName())
                .orElseThrow(ResponseError.NotFound.TAG_NOT_EXISTS::getResponseException);

        return tag;
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
}
