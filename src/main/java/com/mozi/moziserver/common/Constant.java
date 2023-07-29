package com.mozi.moziserver.common;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface Constant {
//    String PROFILE_LOCAL = "local";

    String MDC_KEY_THREAD_ID = "THREAD_ID";
    String MDC_KEY_USER_SEQ = "USER_SEQ";

    String ROLE_USER = "USER";
    Collection<SimpleGrantedAuthority> USER_AUTHORITIES = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + ROLE_USER));
    String ROLE_ADMIN = "ADMIN";
    Collection<SimpleGrantedAuthority> ADMIN_AUTHORITIES = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + ROLE_ADMIN));

    String PW_FIELD_NAME = "pw";

    String CURRENT_PW_FIELD_NAME = "currentPw";

    //TODO fix
    String ID_REGEX = "[A-Za-z0-9]{5,}$";

//    Integer DEFAULT_PAGE = 1;
//    Integer DEFAULT_PAGE_SIZE = 15;

//    String DEFAULT_TIME_ZONE = "Asia/Seoul";
//    String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

//    ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_TIME_ZONE);
//    DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter
//            .ofPattern(DEFAULT_DATE_FORMAT)
//            .withZone(DEFAULT_ZONE_ID);

    String[] PERMIT_ALL_PATHS = new String[]{
            "/health",
            "/api/admin/signin",
            "/api/*/users/signin",
            "/api/*/users/signup",
            "/api/*/users/signout",
            "/api/*/users/find-email",
            "/api/*/users/user-auths",
            "/api/*/users/email-check-auths",
            "/api/*/users/email-check-auths/{token}",
            "/api/*/users/email-check-auths/user-auths",
            "/api/*/users/nicknames",
            "/api/*/users/user-auths/emails",
            "/api/*/challenge-themes",
            "/api/*/users/nicknames/{nickName}/check",
            "/api/*/users/emails/{email}/check",
            "/api/*/users/find-pw",
            "/api/*/find/emails",
            "/api/health-check",
    };

    String[] AUTHENTICATED_PATHS = new String[]{
            "/api/*/users/me"
    };

    String[] ROLE_USER_PATHS = new String[]{
            "/api/**"
    };

    String[] ROLE_ADMIN_PATHS = new String[]{
            "/api/admin/*",
            "/api/admin/**"
    };

    String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    String PW_REGEX = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!%^&*.])[A-Za-z[0-9]!%^&*.]{8,16}$";
    String NICKNAME_REGEX = "^[ㄱ-ㅎ가-힣A-Za-z0-9!@#$%^&*(),.?\":{}|<>]{1,8}$";

    List<List<String>> EMAIL_DOMAIN_GROUPS = Arrays.asList(
            Arrays.asList("gmail.com"),
            Arrays.asList("naver.com"),
            Arrays.asList("daum.net", "hanmail.net"),
            Arrays.asList("kakao.com"),
            Arrays.asList("icloud.com", "me.com", "mac.com"),
            Arrays.asList("devwon.com")
    );

    List<String> IMAGE_MIME_TYPE_LIST = List.of("image/png", "image/jpeg", "image/heic");

    int challengeExtraPoints = 5;

    int islandMaxPoint = 450;

    int islandMaxLevel = 6;

    int lastIslandSeq = 1;

    int lastTurnOfAnimalItem = 2;

    int totalImagesPerIsland = 11;

    List<String> RANDOM_ANIMAL_MENTIONS = Arrays.asList(
            "매주 일요일 위시리스트를 받을 수 있어!",
            "위시리스트 2개를 모으면 내가 이사갈 수 있어!",
            "위시리스트는 일주일에 하나씩 받을 수 있어!",
            "이주 동물마다 필요한 위시 포인트가 달라!"
    );
}
