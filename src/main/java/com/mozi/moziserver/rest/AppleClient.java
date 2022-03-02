package com.mozi.moziserver.rest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "appleClient", url = "${social.apple.url}")
public interface AppleClient {
    @GetMapping("/auth/keys")
    KeyListRes getAuthPublicKeys();

    @Getter
    @Setter
    class KeyListRes {
        private List<KeyRes> keys;
    }

    @Getter
    @Setter
    class KeyRes {
        private String kty;
        private String kid;
        private String alg;
        private String n;
        private String e;
    }
}
