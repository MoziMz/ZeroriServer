package com.mozi.moziserver.model.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
//@EntityListeners(AuditingEntityListener.class)
//@MappedSuperclass
public class AbstractDateTimeEntity {

    //@CreatedDate
    //@Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //@LastModifiedDate
    //@Column(nullable = false)
    private LocalDateTime updatedAt;
}
