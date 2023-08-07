package com.mozi.moziserver.model;

import com.google.firebase.messaging.Message;
import com.mozi.moziserver.common.JsonUtil;
import com.mozi.moziserver.model.mappedenum.FcmMessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AnimalMentionFcmMessage extends FcmMessage {

    private final Long islandSeq;
    private final String animalImgUrl;
    private final List<String> mentionList;

    @Builder
    public AnimalMentionFcmMessage(@NonNull FcmMessageType fcmMessageType, @NonNull LocalDateTime expirationDateTime, @NonNull Long islandSeq, @NonNull String animalImgUrl, @NonNull List<String> mentionList) {

        super(fcmMessageType, expirationDateTime);
        this.islandSeq = islandSeq;
        this.animalImgUrl = animalImgUrl;
        this.mentionList = mentionList;
    }

    @Override
    public Message getMessage(String token) {

        return Message.builder()
                .putData("type", getFcmMessageType().toString())
                .putData("isSilent", "" + getFcmMessageType().isSilent())
                .putData("sendAt", String.valueOf(getSendAt()))
                .putData("expirationDateTime", String.valueOf(getExpirationDateTime()))
                .putData("islandSeq", String.valueOf(islandSeq))
                .putData("animalImgUrl", animalImgUrl)
                .putData("mentions", JsonUtil.toJson(mentionList))
                .setToken(token)
                .build();
    }
}