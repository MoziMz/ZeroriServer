package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.ChallengeDifficultyType;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity(name = "challenge")
public class Challenge extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String name;

    private String description;

    private Integer recommendedCnt;

    @Enumerated(EnumType.STRING)
    private ChallengeTagType tags;

    private Integer currentPlayerCnt;

    private Integer totalPlayerCnt;

    private Integer repeatPlayerCnt;

    private Integer totalCnt;

    private Integer totalChallengeConfirmCnt;

    private Integer repeatRate;

    private Integer point;

    @Enumerated(EnumType.STRING)
    private ChallengeDifficultyType difficulty;

    @OneToMany
    @JoinColumn(name="challenge_seq")
    private List<ChallengeTag> tagList;

    @OneToMany
    @JoinColumn(name="challenge_seq")
    private List<ChallengeTheme> themeList;

    @OneToMany
    @JoinColumn(name="challenge_seq")
    private List<Confirm> confirmList;

}
