package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailAuthType {
    JOIN(1, 3 * 3600),
    RESET_PW(2, 3 * 3600),
    FIND_PW(3,30 * 3600);

    @Getter
    private final int type;
    private final long expiredSeconds;

    public long getExpiredSeconds() {
        return expiredSeconds;
    }

    public static EmailAuthType valueOf(int type) {
        for(EmailAuthType emailAuthType : values())
            if(emailAuthType.getType() == type)
                return emailAuthType;

        return null;
    }
}
