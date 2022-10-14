package com.mozi.moziserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozi.moziserver.rest.KakaoClient;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
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

    @Test
    void generateEcdsaKey() throws Exception {
        final ECKey key = new ECKeyGenerator(Curve.SECP256K1).generate();

        System.out.println("ecKey.json: " + key.toJSONString());
        // System.out.println("ecKey.publicKey: " + Base64.encodeBase64String(key.toRSAPublicKey().getEncoded()));
        // System.out.println("ecKey.privateKey: " + Base64.encodeBase64String(key.toRSAPrivateKey().getEncoded()));
    }

    @Test
    void generateRsaKey() throws Exception {
        final RSAKey key = new RSAKeyGenerator(2048).generate();

        System.out.println("rsaKey.json: " + key.toJSONString());
        // System.out.println("rsaKey.publicKey: " + Base64.encodeBase64String(key.toRSAPublicKey().getEncoded()));
        // System.out.println("rsaKey.privateKey: " + Base64.encodeBase64String(key.toRSAPrivateKey().getEncoded()));
    }
}
