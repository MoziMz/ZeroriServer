package com.mozi.moziserver.model.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "challenge_statistics")
public class ChallengeStatistics extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer year;

    private Integer month;

    @Builder.Default
    private Integer playerFirstTryingCnt = 0;

    @Builder.Default
    private Integer playerConfirmCnt = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_seq")
    private Challenge challenge;
}
