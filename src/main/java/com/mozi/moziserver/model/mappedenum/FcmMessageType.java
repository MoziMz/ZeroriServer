package com.mozi.moziserver.model.mappedenum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FcmMessageType {
    NEW_POST_BOX_MESSAGE(true),
    END_USER_CHALLENGE_MESSAGE(true),
    POSTBOX_MESSAGE_ANIMAL_RECEIVED_ITEM(true),
    POSTBOX_MESSAGE_ANIMAL_NEW_ARRIVED(true);

    private final boolean isSilent;
}
