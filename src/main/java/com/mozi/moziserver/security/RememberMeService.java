package com.mozi.moziserver.security;

import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.repository.UserAuthRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Component
public class RememberMeService implements RememberMeServices {
    private final AbstractServices abstractServices;

    public RememberMeService(
            final UserEmailSignInService userEmailSignInService,
            final UserAuthRepository userAuthRepository,
            @Value("${jwt.key}") final String jwtKeyString
    ) {
        abstractServices = new AbstractServices(userEmailSignInService, userAuthRepository, jwtKeyString);
    }

    @Override
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        return abstractServices.autoLogin(request, response);
    }

    @Override
    public void loginFail(HttpServletRequest request, HttpServletResponse response) {
        abstractServices.cancelCookie(request, response);
    }

    @Override
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        abstractServices.onLoginSuccess(request, response, successfulAuthentication);
    }

    @Slf4j
    public static class AbstractServices extends AbstractRememberMeServices {
        private final UserAuthRepository userAuthRepository;
        private final ECDSASigner signer;
        private final ECDSAVerifier verifier;
        private final Provider bc = BouncyCastleProviderSingleton.getInstance();

        public AbstractServices(
                final UserDetailsService userDetailsService,
                final UserAuthRepository userAuthRepository,
                final String jwtKeyString
        ) {
            super("remember-me", userDetailsService);
            this.userAuthRepository = userAuthRepository;
            try {
                final ECKey key = ECKey.parse(jwtKeyString);
                signer = new ECDSASigner(key);
                signer.getJCAContext().setProvider(BouncyCastleProviderSingleton.getInstance());
                verifier = new ECDSAVerifier(key);
                verifier.getJCAContext().setProvider(BouncyCastleProviderSingleton.getInstance());
                Security.addProvider(bc);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getCause());
            }
            setTokenValiditySeconds(39 * 24 * 3600); // 쿠키 유효시간 30일
        }
        @Override
        protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
            if (successfulAuthentication.getPrincipal() instanceof ResUserSignIn) {
                final long userAuthSeq = ((ResUserSignIn) successfulAuthentication.getPrincipal()).getUserAuthSeq();
                final String jwtToken = createJwtToken(userAuthSeq);
                setCookie(new String[]{jwtToken}, getTokenValiditySeconds(), request, response);
            }
        }

        private String createJwtToken(final Long userAuthSeq) {
            Calendar calendar = Calendar.getInstance();
            final Date iat = calendar.getTime();
            calendar.add(Calendar.SECOND, getTokenValiditySeconds());
            final Date exp = calendar.getTime();

            final SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.ES256K)
                            .type(JOSEObjectType.JWT)
                            .build(),
                    new JWTClaimsSet.Builder()
                            .issuer("https://zero-ri.com")
                            .issueTime(iat)
                            .expirationTime(exp)
                            .audience("com.mozi.moziserver")
                            .subject(userAuthSeq.toString())
                            .build());

            try {
                signedJWT.sign(signer);
            } catch (JOSEException e) {
                e.printStackTrace();
            }

            return signedJWT.serialize();
        }

        @Override
        protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) throws RememberMeAuthenticationException, UsernameNotFoundException {
            if (cookieTokens.length != 1) {
                log.error("cookie length = " +  cookieTokens.length + " / " + Arrays.toString(cookieTokens));
                throw new RememberMeAuthenticationException("Unknown cookie");
            }

            long userAuthSeq;
            Date exp;
            try {
                SignedJWT signedJWT = SignedJWT.parse(cookieTokens[0]);
                if (!signedJWT.verify(verifier)) {
                    log.error("Invalid JWT : " + cookieTokens[0]);
                    return createAnonymousUser();
                }
                userAuthSeq = Long.parseLong(signedJWT.getJWTClaimsSet().getSubject());
                exp = signedJWT.getJWTClaimsSet().getExpirationTime();
            } catch (Exception e) {
                e.printStackTrace();
                cancelCookie(request, response);
                return createAnonymousUser();
            }

            final Optional<UserAuth> userAuth = userAuthRepository.findById(userAuthSeq);
            if (userAuth.isEmpty()) {
                return createAnonymousUser();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, 30 * 24 * 3600); // 30일 미만 남으면 재발급
            if (exp.before(calendar.getTime())) {
                final String jwtToken = createJwtToken(userAuthSeq);
                setCookie(new String[]{jwtToken}, getTokenValiditySeconds(), request, response);
            }
            return new ResUserSignIn(userAuth.get());
        }

        private User createAnonymousUser() {
            return new User("anonymous", "", false, false, false, false, AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        }

        @Override
        protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
            return new ResUserRememberSignIn((ResUserSignIn) user);
        }

        public void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
            super.cancelCookie(request, response);
        }
    }
}
