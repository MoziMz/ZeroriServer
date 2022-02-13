package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "challenge_scrap")
@IdClass(ChallengeUserSeq.class)
public class ChallengeScrap extends AbstractTimeEntity{

    @Id
    private Long userSeq;

    @Id
    private Long challengeSeq;
}
