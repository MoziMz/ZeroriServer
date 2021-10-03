package com.mozi.moziserver.common;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;

public interface Constant {
//    String PROFILE_LOCAL = "local";

    String MDC_KEY_THREAD_ID = "THREAD_ID";
    String MDC_KEY_USER_SEQ = "USER_SEQ"; // TODO check user_seq

    String ROLE_USER = "USER";
    Collection<SimpleGrantedAuthority> USER_AUTHORITIES = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + ROLE_USER));

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
            "/api/*/users/signout"
    };

    String[] AUTHENTICATED_PATHS = new String[]{
            "/api/*/users/me"
    };

    String[] ROLE_USER_PATHS = new String[]{
            "/api/**"
    };
}
