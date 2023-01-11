package com.mozi.moziserver.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "kakao-client", url = "${social.kakao.url}")
public interface KakaoClient {

    //
    @GetMapping("/v1/user/access_token_info")
    ResponseEntity<ResUserByAccessToken> getUserMeByAccessToken(
            @RequestHeader("Authorization") String accessToken // "Bearer {accessToken}" // FIXME
    );

    @Getter
    @Setter
    class ResUserByAccessToken {
        private Long id;
        @JsonProperty("expires_in")
        private Long expiresInMillis;
        @JsonProperty("app_id")
        private Long appId;
    }

//    https://developers.kakao.com/docs/latest/ko/user-mgmt/rest-api

//    GET /v1/user/access_token_info HTTP/1.1
//    Host: kapi.kakao.com
//    Authorization: Bearer {access_token}
//    Content-type: application/x-www-form-urlencoded;charset=utf-8

//    HTTP/1.1 200 OK
//    {
//        "id":123456789,
//        "expiresInMillis":239036,
//        "appId":1234
//    }

    @GetMapping("/v1/user/me")
    ResponseEntity<ResUserByAdminKey> getUserByAdminKey(
            @RequestHeader("Authorization") String adminKey, // "KakaoAK ${social.kakao.adminkey}"
            @RequestParam("target_id_type") String targetIdType, // "user_id"
            @RequestParam("target_id") String targetId, // user id,
            @RequestParam("property_keys") String propertyKeys
    );

    @Getter
    @Setter
    class ResUserByAdminKey {
        private Long id;
        //private Date connectedAt;
        private KakaoAccount kakaoAccount;
    }

    @Getter
    @Setter
    class KakaoAccount {
        private Boolean hasEmail;
        private Boolean emailNeedsAgreement;
        private Boolean isEmailValid; // if not emailNeedsAgreement then null
        private Boolean isEmailVerified; // if not emailNeedsAgreement then null
        private String email; // if not emailNeedsAgreement then null
        private Profile profile;
    }

    @Getter
    @Setter
    class Profile {
        private String nickname;
    }
}
