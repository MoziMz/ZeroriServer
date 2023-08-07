package com.mozi.moziserver.model;

import com.google.firebase.messaging.Message;
import com.mozi.moziserver.model.mappedenum.FcmMessageType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FcmMessage {

    private final FcmMessageType fcmMessageType;
    private final LocalDateTime sendAt;
    private final LocalDateTime expirationDateTime;

    protected FcmMessage(FcmMessageType fcmMessageType, LocalDateTime expirationDateTime) {

        this.fcmMessageType = fcmMessageType;
        this.sendAt = LocalDateTime.now();
        this.expirationDateTime = expirationDateTime;
    }

    public Message getMessage(String token) {

        return Message.builder()
                .putData("type", getFcmMessageType().toString())
                .putData("isSilent", "" + getFcmMessageType().isSilent())
                .putData("sendAt", String.valueOf(getSendAt()))
                .putData("expirationDateTime", String.valueOf(getExpirationDateTime()))
                .setToken(token)
                .build();
    }

    // TODO 이후, 나올 수 있는 메서드 예시 (FirebaseMessaging.getInstance()의 메서드 형태에 따라..)
    // public Message getTopicMessage()
    // public Multiple MulticastMessage getMulticastMessage
    // 등..
}