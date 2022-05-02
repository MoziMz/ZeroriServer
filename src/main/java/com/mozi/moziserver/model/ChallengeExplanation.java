package com.mozi.moziserver.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeExplanation {
    private String title;

    private List<ChallengeExplanationContent> contents;
}
