package com.mozi.moziserver.model.entity;

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

    private ChallengeTagType tags;

    private String currentPlayerCnt;

    private Integer totalPlayerCnt;

    private Integer repeatPlayerCnt;

    private Integer totalCnt;

    private Integer totalChallengeConfirmCnt;

    private Integer repeatRate;

    private Integer point;

    private Integer difficulty;

    @OneToMany
    @JoinColumn(name="challeng_seq")
    private List<ChallengeTag> tagList;

}
