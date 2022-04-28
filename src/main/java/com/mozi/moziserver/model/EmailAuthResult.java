package com.mozi.moziserver.model;

import com.mozi.moziserver.model.mappedenum.EmailAuthType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailAuthResult {
    private EmailAuthType type;
    private Status status;

    public enum Status {
        SUCC,
        INVALID,
        ALREADY_SUCC
    }
}
