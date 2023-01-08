package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserNoticeType {
    POSTBOX_MESSAGE_ANIMAL_RECEIVED_ITEM(1);

    @Getter
    private final int type;

    public static UserNoticeType valueOf(int type) {
        for (UserNoticeType userNoticeType : values())
            if (userNoticeType.getType() == type)
                return userNoticeType;

        return null;
    }

}
