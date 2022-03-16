package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.ChallengeDifficultyType;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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

    @Enumerated(EnumType.STRING)
    private ChallengeTagType mainTag; // 현재는 챌린지당 하나의 태그만 있다.

    private Long themeSeq; // 조인 안함 -> 클라이언트가 앱실행시 챌린지테마 테이블을 호출해서 테마정보 가지고 있음

    private Integer point;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="challenge_seq")
    private List<ChallengeTag> tagList;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="challenge_seq")
    private List<Confirm> confirmList;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "challenge", optional = false)
    ChallengeRecord challengeRecord;
}
