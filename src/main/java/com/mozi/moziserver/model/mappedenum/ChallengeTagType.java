package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.Environment;

@Getter
@RequiredArgsConstructor
public enum ChallengeTagType {
    다시쓰기,
    쓰지않기,
    잘버리기
}
