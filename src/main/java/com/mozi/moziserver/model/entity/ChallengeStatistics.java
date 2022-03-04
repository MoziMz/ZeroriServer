package com.mozi.moziserver.model.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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

    private LocalDate startDate;

    @Builder.Default
    private Integer playerFirstTryingCnt = 0;

    @Builder.Default
    private Integer playerConfirmCnt = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_seq")
    private Challenge challenge;
}
