package com.mozi.moziserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozi.moziserver.rest.KakaoClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class MoziServerApplicationTests {
    @Autowired
    KakaoClient kakaoClient;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoads() throws JsonProcessingException {
        ResponseEntity<KakaoClient.ResUserByAccessToken> res = kakaoClient.getUserMeByAccessToken("Bearer iobCxUKtm-yj-VYoLP370xaKz8KlauaboiZKOgopcBMAAAF9QW6aFg");
        System.out.println(objectMapper.writeValueAsString(res.getBody()));
    }

}
