package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.ChallengeExplanation;
import com.mozi.moziserver.model.mappedenum.ChallengeStateType;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import com.mozi.moziserver.model.mappedenum.ExplanationConverter;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "challenge")
public class Challenge extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String name;

    private String description;

    private Integer recommendedCnt;

    private Long themeSeq; // 조인 안함 -> 클라이언트가 앱실행시 챌린지테마 테이블을 호출해서 테마정보 가지고 있음

    private Integer point;

    @Enumerated(EnumType.STRING)
    private ChallengeTagType mainTag; // 현재는 챌린지당 하나의 태그만 있다.

    @Enumerated(EnumType.STRING)
    private ChallengeStateType state;

    @Convert(converter = ExplanationConverter.class)
    private ChallengeExplanation explanation;

    @Transient
    ChallengeRecord challengeRecord;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_seq")
    private List<ChallengeTag> challengeTagList;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_seq")
    private List<Confirm> confirmList;
}
