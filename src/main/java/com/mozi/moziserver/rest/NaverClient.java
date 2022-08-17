package com.mozi.moziserver.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "naver-client", url = "${social.naver.url}")
public interface NaverClient {
    @GetMapping("/v1/nid/me")
    ResponseEntity<NaverClient.ResUserByAccessToken> getUserMeByAccessToken(
            @RequestHeader("Authorization") String accessToken // "Bearer {accessToken}" // FIXME
    );

    @Getter
    @Setter
    class ResUserByAccessToken {
        private Response response;
    }
    @Getter
    @Setter
    class Response {
        private String email;
        private String id;
    }

//    https://developers.naver.com/docs/login/profile/profile.md

//    GET v1/nid/me HTTP/1.1
//    Host: openapi.naver.com
//    User-Agent: curl/7.43.0
//    Accept: */*

//    HTTP/1.1 200 OK
//    {
//        "response": {
//          "email": "openapi@naver.com",
//          "id": "32742776",
//         }
//    }

}
