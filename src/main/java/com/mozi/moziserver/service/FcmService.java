package com.mozi.moziserver.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserFcm;
import com.mozi.moziserver.model.mappedenum.FcmMessageType;
import com.mozi.moziserver.repository.UserFcmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

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
            log.error("FirebaseMessagingException", e);
            if (e.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)) {
                UserFcm userFcm = userFcmRepository.findByToken(token);
                userFcm.setState(Boolean.FALSE);
                userFcmRepository.save(userFcm);
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
            log.error("FirebaseMessagingException", e);
        }
    }

    public void sendMessageToUser(User user, FcmMessageType type) {
        List<UserFcm> userFcmList = userFcmRepository.findAllByUserSeqAndState(user.getSeq(), Boolean.TRUE);
        // TODO userFcmList 길이가 0일 때 에러처리 추가

        for(UserFcm userFcm : userFcmList) {
            sendMessage(userFcm.getToken(), FcmMessageType.NEW_POST_BOX_MESSAGE);
        }
    }
}
