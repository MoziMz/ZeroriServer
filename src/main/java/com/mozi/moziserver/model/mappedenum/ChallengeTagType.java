package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.Environment;

@Getter
@RequiredArgsConstructor
public enum ChallengeTagType {
    INDOOR("실내"),
    OUTDOOR("실외"),
    ONEDAY("하루챌린지");

    private final String name;
}
