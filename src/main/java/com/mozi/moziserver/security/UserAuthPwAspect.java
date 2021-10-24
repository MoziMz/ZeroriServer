package com.mozi.moziserver.security;

import com.mozi.moziserver.model.entity.UserAuth;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

//@Aspect
//@Component
@RequiredArgsConstructor
public class UserAuthPwAspect {
    private final Pattern BCRYPT_PATTERN = Pattern
            .compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

    @Value("${security.password.keyword}")
    private String keyword;

    private final PasswordEncoder passwordEncoder;

    @Before(value = "execution(* EmailAuthService.*(*)) " +
            "&& @annotation(AutoPwEncrypt) " +
            "&& args(userAuth)")
    public void onAutoPwEncryptServiceBefore(UserAuth userAuth) {
        if (userAuth != null
                && !StringUtils.isEmpty(userAuth.getPw())
                && !BCRYPT_PATTERN.matcher(userAuth.getPw()).matches()) {
            userAuth.setPw(encryptPassword(userAuth));
        }
    }

    @Before(value = "execution(* UserAuthRepository.*(*)) " +
            "&& @annotation(AutoPwEncrypt) " +
            "&& args(userAuth)")
    public void onAutoPwEncryptRepositoryBefore(UserAuth userAuth) {
        if (userAuth != null
                && !StringUtils.isEmpty(userAuth.getPw())
                && !BCRYPT_PATTERN.matcher(userAuth.getPw()).matches()) {
            userAuth.setPw(encryptPassword(userAuth));
        }
    }

    private String encryptPassword(UserAuth userAuth) {
        return passwordEncoder.encode(getRawPassword(userAuth.getId(), userAuth.getPw()));
    }

    public boolean passwordMatches(String id, String pw, String encryptPw) {
        return passwordEncoder.matches(getRawPassword(id, pw), encryptPw);
    }

    private String getRawPassword(String id, String pw) {
        return id + keyword + pw;
    }
}