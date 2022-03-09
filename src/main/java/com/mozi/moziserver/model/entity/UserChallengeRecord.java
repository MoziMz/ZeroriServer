package com.mozi.moziserver.model.entity;

import lombok.Builder;

import javax.persistence.*;

@Entity
public class UserChallengeRecord extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer confirmCnt;

    @Builder.Default
    private Integer acquisitionPoint = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_seq")
    private Challenge challenge;


}
