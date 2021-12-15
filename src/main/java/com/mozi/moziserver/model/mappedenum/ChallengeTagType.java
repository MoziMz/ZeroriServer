package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.Environment;

@Getter
@RequiredArgsConstructor
public enum ChallengeTagType {
    실내("실내"),
    환경("환경"),
    실외("실외");
    
    private final String name;
}
