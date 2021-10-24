package com.mozi.moziserver.security;

import com.mozi.moziserver.model.entity.RememberMeToken;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.repository.RememberMeTokenRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Slf4j
public class RememberMeFilter extends OncePerRequestFilter implements RememberMeServices, InitializingBean, LogoutHandler {
    private static final String COOKIE_KEY = "remember-me";
    private static final String DEFAULT_PARAMETER = "remember-me";
    private static final String DELIMITER = ":";
    private static final int DEFAULT_SERIES_LENGTH = 16;
    private static final int DEFAULT_TOKEN_LENGTH = 16;

    private final RememberMeTokenRepository tokenRepository;
    private final UserRepository userRepository;

    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    private final String cookieName = COOKIE_KEY;
    private final String cookieDomain = null;
    private final String parameter = DEFAULT_PARAMETER;
    private final String key;
    private final int tokenValiditySeconds = 30 * 24 * 3600;
    private final Boolean useSecureCookie = false; // TODO : https

    private final SecureRandom random = new SecureRandom();

    private final int seriesLength = DEFAULT_SERIES_LENGTH;
    private final int tokenLength = DEFAULT_TOKEN_LENGTH;

    public RememberMeFilter(String key, RememberMeTokenRepository tokenRepository, UserRepository userRepository) {
        Assert.hasLength(key, "key cannot be empty or null");
        Assert.notNull(tokenRepository, "A RememberMeTokenRepository is required");
        Assert.notNull(userRepository, "A UserRepository is required");
        this.key = key;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.hasLength(key, "key cannot be empty or null");
        Assert.notNull(tokenRepository, "A RememberMeTokenRepository is required");
        Assert.notNull(userRepository, "A UserRepository is required");
    }

    @Override
    public final Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        String rememberMeCookie = extractRememberMeCookie(request);

        if (StringUtils.isEmpty(rememberMeCookie)) {
            if (rememberMeCookie != null) {
                cancelCookie(request, response);
            }
            return null;
        }

        try {
            String[] cookieTokens = decodeCookie(rememberMeCookie);
            User user = processAutoLoginCookie(cookieTokens, request, response);

            // TODO : Check User State
            if (user == null) {
                log.debug("Remember-me user not found.");
            } else {
                log.debug("Remember-me cookie accepted");
                return new RememberMeAuthentication(user, authenticationDetailsSource.buildDetails(request));
            }
        } catch (CookieTheftException cte) {
            cancelCookie(request, response);
            throw cte;
        } catch (InvalidCookieException invalidCookie) {
            log.debug("Invalid remember-me cookie: " + invalidCookie.getMessage());
        } catch (RememberMeAuthenticationException e) {
            log.debug(e.getMessage());
        }

        cancelCookie(request, response);
        return null;
    }

    public String extractRememberMeCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public String[] decodeCookie(String cookieValue) throws InvalidCookieException {
        for (int j = 0; j < cookieValue.length() % 4; j++) {
            cookieValue = cookieValue + "=";
        }

        byte[] decodedBytes;
        try {
            decodedBytes = Base64.getDecoder().decode(cookieValue.getBytes());
        } catch (IllegalArgumentException e) {
            throw new InvalidCookieException("Cookie token was not Base64 encoded; value was '" + cookieValue + "'");
        }

        String[] tokens = StringUtils.delimitedListToStringArray(new String(decodedBytes), DELIMITER);

        for (int i = 0; i < tokens.length; i++) {
            try {
                tokens[i] = URLDecoder.decode(tokens[i], StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }

        return tokens;
    }

    public String encodeCookie(String[] cookieTokens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cookieTokens.length; i++) {
            try {
                sb.append(URLEncoder.encode(cookieTokens[i], StandardCharsets.UTF_8.toString()));
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }

            if (i < cookieTokens.length - 1) {
                sb.append(DELIMITER);
            }
        }

        String value = sb.toString();

        sb = new StringBuilder(new String(Base64.getEncoder().encode(value.getBytes())));

        while (sb.charAt(sb.length() - 1) == '=') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    private User processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2 + " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];

        Optional<RememberMeToken> tokenOptional = tokenRepository.findRememberMeTokenBySeries(presentedSeries);

        if (tokenOptional.isEmpty()) {
            throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
        }

        RememberMeToken token = tokenOptional.get();

        if (!presentedToken.equals(token.getToken())) {
            tokenRepository.delete(token);
            throw new CookieTheftException("Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.");
        }

        if (token.getLastUsed().getTime() + tokenValiditySeconds * 1000L < System.currentTimeMillis()) {
            throw new RememberMeAuthenticationException("Remember-me login has expired");
        }

        log.debug("Refreshing persistent login token for user '" + token.getUser().getSeq() + "', series '" + token.getSeries() + "'");

        RememberMeToken newToken = RememberMeToken.builder()
                .user(token.getUser())
                .series(token.getSeries())
                .token(generateTokenData())
                .build();

        try {
            token = tokenRepository.save(newToken);
            addCookie(newToken, request, response);
        } catch (Exception e) {
            log.error("Failed to update token: ", e);
            throw new RememberMeAuthenticationException("Autologin failed due to data access problem");
        }

        return token.getUser();
    }

    @Override
    public final void loginFail(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Interactive login attempt was unsuccessful.");
        cancelCookie(request, response);
    }

    @Override
    public final void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        if (!rememberMeRequested(request, parameter)) {
            log.debug("Remember-me login not requested.");
            return;
        }

        User user = (User) successfulAuthentication.getPrincipal();

        log.debug("Creating new persistent login for user " + user.getSeq());

        RememberMeToken token = RememberMeToken.builder()
                .user(user)
                .series(generateSeriesData())
                .token(generateTokenData())
                .lastUsed(new Date())
                .build();

        try {
            token = tokenRepository.save(token);
            addCookie(token, request, response);
        } catch (Exception e) {
            log.error("Failed to save persistent token ", e);
            throw new RuntimeException("Fail to insert RememberMeToken");
        }
    }

    public boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        String paramValue = request.getParameter(parameter);

        if (paramValue == null) {
            return true;
        }
        if (paramValue.equalsIgnoreCase("false") || paramValue.equalsIgnoreCase("off")
                || paramValue.equalsIgnoreCase("no") || paramValue.equals("0")) {
            return false;
        }

        log.debug("Did not send remember-me cookie (principal did not set parameter '" + parameter + "')");

        return true;
    }

    public void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Cancelling cookie");
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath(getCookiePath(request));
        if (cookieDomain != null) {
            cookie.setDomain(cookieDomain);
        }
        response.addCookie(cookie);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.debug("Logout of user " + (authentication == null ? "Unknown" : authentication.getName()));
        Long userSeq = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            userSeq = ((User) authentication.getPrincipal()).getSeq();
        }

        logout(request, response, userSeq);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, Long userSeq) {
        cancelCookie(request, response);

        if (userSeq != null) {
//            final User user = userRepository.findById(userSeq).get(); // SELECT * FROM user WHERE seq = #{userSeq}
//            final List<RememberMeToken> rememberMeTokenList = tokenRepository.findRememberMeTokensByUser(user); // SELECT * FROM remember_me_token WHERE user_seq = #{user.seq}
//            tokenRepository.deleteAll(rememberMeTokenList); // DELETE FROM remember_me_token WHERE series IN (~~~)
            tokenRepository.deleteRememberMeTokensByUserSeq(userSeq);
        }
    }

    public String generateSeriesData() {
        return generateData(seriesLength);
    }

    public String generateTokenData() {
        return generateData(tokenLength);
    }

    private String generateData(int length) {
        byte[] newData = new byte[length];
        random.nextBytes(newData);
        return new String(Base64.getEncoder().encode(newData));
    }

    private void addCookie(RememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = encodeCookie(new String[]{token.getSeries(), token.getToken()});
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(tokenValiditySeconds);
        cookie.setPath(getCookiePath(request));
        if (cookieDomain != null) {
            cookie.setDomain(cookieDomain);
        }
        if (tokenValiditySeconds < 1) {
            cookie.setVersion(1);
        }
        cookie.setSecure(useSecureCookie != null ? useSecureCookie : request.isSecure());
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }

    private String getCookiePath(HttpServletRequest request) {
        return !StringUtils.isEmpty(request.getContextPath()) ? request.getContextPath() : "/";
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isApiRequest = request.getRequestURI().startsWith("/api");

        if (!isApiRequest) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication prevAuth = SecurityContextHolder.getContext().getAuthentication();
        boolean isPrevAuthenticated = prevAuth != null && prevAuth.isAuthenticated();
        boolean isAuthenticated = false;
        if (!isPrevAuthenticated) {
            Authentication auth = autoLogin(request, response);
            if (auth != null && auth.isAuthenticated()) {
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(auth);
                request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
                isAuthenticated = true;
            }
        }
        isAuthenticated = isPrevAuthenticated || isAuthenticated;

        filterChain.doFilter(request, response);

        Authentication postAuth = SecurityContextHolder.getContext().getAuthentication();
        boolean isApiAuthenticated = postAuth instanceof com.mozi.moziserver.security.UserAuthentication && postAuth.isAuthenticated();

        if (!isAuthenticated && isApiAuthenticated) {
            loginSuccess(request, response, postAuth);
        }
        if (isAuthenticated && !isApiAuthenticated) {
            logout(request, response, postAuth);
        }
    }
}