package com.mozi.moziserver.model.entity;

import lombok.Getter;

import jakarta.persistence.*;

@Getter
@Entity(name = "animal_mention")
public class AnimalMention extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer itemTurn;

    private Integer startPoint;

    private Integer rangePoint;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_seq")
    private Animal animal;
}