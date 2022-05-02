package com.mozi.moziserver.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeExplanationContent {
    private Integer turn;

    private String content;
}
