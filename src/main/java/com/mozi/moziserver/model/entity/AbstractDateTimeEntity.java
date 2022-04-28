package com.mozi.moziserver.model.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AbstractDateTimeEntity {

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
