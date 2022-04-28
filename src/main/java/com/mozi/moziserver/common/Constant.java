package com.mozi.moziserver.common;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface Constant {
//    String PROFILE_LOCAL = "local";

    String MDC_KEY_THREAD_ID = "THREAD_ID";
    String MDC_KEY_USER_SEQ = "USER_SEQ"; // TODO check user_seq

    String ROLE_USER = "USER";
    Collection<SimpleGrantedAuthority> USER_AUTHORITIES = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + ROLE_USER));

    //FIXME
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
            "/api/*/challenge-themes"
    };

    String[] AUTHENTICATED_PATHS = new String[]{
            "/api/*/users/me"
    };

    String[] ROLE_USER_PATHS = new String[]{
            "/api/**"
    };

    String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    List<List<String>> EMAIL_DOMAIN_GROUPS = Arrays.asList(
            Arrays.asList("gmail.com"),
            Arrays.asList("naver.com"),
            Arrays.asList("daum.net", "hanmail.net"),
            Arrays.asList("kakao.com"),
            Arrays.asList("icloud.com", "me.com", "mac.com"),
            Arrays.asList("devwon.com")
    );

    List<String> IMAGE_MIME_TYPE_LIST = List.of("image/png", "image/jpeg");
}
