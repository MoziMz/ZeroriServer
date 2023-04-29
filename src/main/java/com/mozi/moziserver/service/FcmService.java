package com.mozi.moziserver.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserFcm;
import com.mozi.moziserver.model.mappedenum.FcmMessageType;
import com.mozi.moziserver.repository.UserFcmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final UserFcmRepository userFcmRepository;

    private void sendMessage(String token, FcmMessageType type) {
        Message message = Message.builder()
                .putData("isSilent", "" + type.isSilent())
                .putData("type", type.toString())
                .setToken(token)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.debug(response);
        } catch (FirebaseMessagingException e) {
            log.error("FIREBASE_MESSAGING_EXCEPTION", e);
            if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                log.error("UNREGISTERED_TOKEN", e);
            }
        }
    }

    private void sendDateMessage(String token, LocalDate date, FcmMessageType type) {
        Message message = Message.builder()
                .putData("isSilent", "" + type.isSilent())
                .putData("type", type.toString())
                .putData("date", date.toString())
                .setToken(token)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.debug(response);
        } catch (FirebaseMessagingException e) {
            log.error("FIREBASE_MESSAGING_EXCEPTION", e);
            if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                log.error("UNREGISTERED_TOKEN", e);
            }
        }
    }

    public void sendMessageToAll(FcmMessageType type) {
        Message message = Message.builder()
                .putData("isSilent", "" + type.isSilent())
                .putData("type", type.toString())
                .setTopic("APP")
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.debug(response);
        } catch (FirebaseMessagingException e) {
            log.error("FIREBASE_MESSAGING_EXCEPTION", e);
        }
    }

    public void sendMessageToUser(User user, FcmMessageType type) {
        UserFcm userFcm = userFcmRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("USER_FCM_TOKEN_IS_NOT_EXIST"));

        sendMessage(userFcm.getToken(), type);
    }

    public void sendDateMessageToUser(User user, LocalDate date, FcmMessageType type) {
        UserFcm userFcm = userFcmRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("USER_FCM_TOKEN_IS_NOT_EXIST"));

        sendDateMessage(userFcm.getToken(), date, type);
    }
}
