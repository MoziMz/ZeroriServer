package com.mozi.moziserver.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    private String tags;

    private String currentPlayerCnt;

    private Integer totalPlayerCnt;

    private Integer repeatPlayerCnt;

    private Integer totalCnt;

    private Integer totalChallengeConfirmCnt;

    private Integer repeatRate;

    private Integer point;

    private Integer difficulty;

}
