package com.mozi.moziserver.model.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeUserSeq implements Serializable {

    private Long challengeSeq;

    private Long userSeq;
}
