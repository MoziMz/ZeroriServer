package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FcmMessageType {
    NEW_POST_BOX_MESSAGE(true),
    END_USER_CHALLENGE_MESSAGE(true),
    USER_NOTICE_POSTBOX_MESSAGE_ANIMAL_MESSAGE(true);


    private final boolean isSilent;
}
