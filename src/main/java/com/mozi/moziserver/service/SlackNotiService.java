package com.mozi.moziserver.service;

import com.mozi.moziserver.rest.SlackClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackNotiService {

    private final SlackClient slackClient;

    public void sendConfirmBlockedMessage(String challengeName,Long confirmSeq, String imgUrl) {
        String payload = String.format(
                "{\"text\": \"🔴 *[Challenge Image Blocked]*\\n- *Challenge Name:* `%s`\\n- *Confirm Seq:* `%s`\\n- *Image URL:* `<%s>`\\n\\n⚠️ 인증 이미지가 챌린지에 맞지 않아 차단되었습니다.\"}",
                challengeName, confirmSeq, imgUrl
        );
        slackClient.sendMessage(payload);
    }
}
