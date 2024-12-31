package com.mozi.moziserver.model;

import com.mozi.moziserver.model.mappedenum.UserChallengeResultType;
import lombok.*;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChallengeResult {
    private Integer turn;

    @Enumerated(EnumType.STRING)
    private UserChallengeResultType result;
}