package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.ChallengeDifficultyType;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Getter
public class ResChallenge {
    private Long seq;

    private String name;

    private String description;

    private Integer recommendedCnt;

    private String mainTag;

    private Integer point;

    private Long themeSeq;

    private LocalDateTime updatedAt;  // 가장 최신 인증 시간 (몇 분 전에 인증했어요)

    private ResUserChallenge userChallenge;

    private List<ResChallengeStatistics> statisticsList;

    private ResUserChallengeRecord userChallengeRecord;

    private ResChallengeRecord challengeRecord;

    private List<ResChallengeTagList> tagList;

    private Boolean isScrap;

    private ResChallenge(Challenge challenge, Optional<UserChallenge> optionalUserChallenge, Optional<UserChallengeRecord> optionalUserChallengeRecord, List<ChallengeStatistics> challengeStatisticsList, List<ResChallengeTagList> challengeTag,ChallengeScrap challengeScrap) {
        this.seq = challenge.getSeq();
        this.name = challenge.getName();
        this.description = challenge.getDescription();
        this.recommendedCnt = challenge.getRecommendedCnt();
        this.mainTag = challenge.getMainTag().getName();
        this.point = challenge.getPoint();
        this.themeSeq = challenge.getThemeSeq();
        this.updatedAt = challenge.getUpdatedAt();
        this.userChallenge = optionalUserChallenge
                .map(ResUserChallenge::of)
                .orElse(null);
        this.statisticsList = Optional.ofNullable(challengeStatisticsList)
                .stream()
                .flatMap(Collection::stream)
                .map(ResChallengeStatistics::of)
                .collect(Collectors.toList());
        this.userChallengeRecord = optionalUserChallengeRecord
                .map(ResUserChallengeRecord::of)
                .orElse(null);
        this.challengeRecord = ResChallengeRecord.of(challenge.getChallengeRecord());
        this.tagList = challenge.getTagList().stream().map(ResChallengeTagList::of).collect(Collectors.toList());
        this.isScrap=challengeScrap!=null?true:false;
    }

    public static ResChallenge of(Challenge challenge, Optional<UserChallenge> optionalUserChallenge, Optional<UserChallengeRecord> optionalUserChallengeRecord, List<ChallengeStatistics> challengeStatisticsList, List<ResChallengeTagList> challengeTagList,ChallengeScrap challengeScrap) {
        return new ResChallenge(challenge, optionalUserChallenge, optionalUserChallengeRecord, challengeStatisticsList, challengeTagList,challengeScrap);
    }
}
