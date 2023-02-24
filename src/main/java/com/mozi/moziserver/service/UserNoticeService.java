package com.mozi.moziserver.service;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserNotice;
import com.mozi.moziserver.model.mappedenum.UserNoticeType;
import com.mozi.moziserver.repository.UserNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNoticeService {

    private final UserNoticeRepository userNoticeRepository;

    public void upsertUserNotice(User user, UserNoticeType type, Long relatedSeq) {

        UserNotice userNotice = userNoticeRepository.findByUserAndType(user, type)
                .orElse(
                        UserNotice.builder()
                                .user(user)
                                .type(type)
                                .relatedSeq(relatedSeq)
                                .checkedState(false)
                                .build()
                );

        userNotice.setRelatedSeq(relatedSeq);
        userNotice.setCheckedState(false);

        userNoticeRepository.save(userNotice);
    }
}