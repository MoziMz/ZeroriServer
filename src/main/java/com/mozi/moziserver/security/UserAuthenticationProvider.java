package com.mozi.moziserver.security;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.repository.UserAuthRepository;
import com.mozi.moziserver.repository.UserRepository;
import com.mozi.moziserver.rest.KakaoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

// TODO
@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserAuthRepository userAuthRepository;
    private final UserRepository userRepository;

    private final KakaoClient kakaoClient;

    @Value("${social.kakao.appId}")
    private Long kakaoAppId;

//    @Autowired
//    UserAuthPwAspect userAuthPwAspect;

//    @Autowired
//    FacebookRestClient facebookClient;
//    @Value("${social.facebook.appid}")
//    String facebookAppId;
//    @Value("${social.facebook.secret}")
//    String facebookSecret;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof com.mozi.moziserver.security.UserAuthToken))
            return null;

        com.mozi.moziserver.security.UserAuthToken userAuthToken = (com.mozi.moziserver.security.UserAuthToken) authentication;

        if (userAuthToken.getType() == null || userAuthToken.getId() == null)
            return null;

        UserAuth userAuth = new UserAuth();
        userAuth.setType(userAuthToken.getType());
        userAuth.setId(userAuthToken.getId());
        userAuth.setPw(userAuthToken.getPw());

        String socialId = null;
//
//        if (userAuth.getType().isSocial()) {
//            socialId =
//                    userAuth.getType() == UserAuthType.KAKAO ? getKakaoSocialId(userAuth.getId()) : null;
////                type == UserAuthType.FACEBOOK ? getFacebookSocialId(userAuth) :
////                type == UserAuthType.NAVER ? getNaverSocialId(userAuth) :
////                type == UserAuthType.GOOGLE ? getGoogleSocialId(userAuth)
//
//            if (socialId == null)
//                return new com.mozi.moziserver.security.UserAuthentication();
//        }

        User user = userAuth.getType() == UserAuthType.ID
                ? getUserSeqByTypeId(userAuth)
                : getUserSeqByTypeAndSocialId(userAuth.getType(), socialId);

        return user == null ? new com.mozi.moziserver.security.UserAuthentication() : new com.mozi.moziserver.security.UserAuthentication(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(com.mozi.moziserver.security.UserAuthToken.class);
    }

    private User getUserSeqByTypeId(UserAuth userAuth) {
        final String rawPassword = userAuth.getPw();

        return userAuthRepository.findUserAuthByTypeAndId(userAuth.getType(), userAuth.getId())
                .filter(uAuth -> uAuth.getPw().equals(rawPassword))
                .map(UserAuth::getUser)
                .orElse(null);

//        Optional<UserAuth> userAuthOptional = userAuthRepository.findUserAuthByTypeAndId(userAuth.getType(), userAuth.getId());
//
//        if (userAuthOptional.isEmpty()
//                -uAuth -> uAuth.getPw().equals(rawPassword))
////                TODO || 암호화 적용하면 이렇게 바꿈 !userAuthPwAspect.passwordMatches(userAuth.getId(), rawPassword, userAuth.getPw()))
//            return null;
//
//        return userAuthOptional.get().getUser();
    }

    private User getUserSeqByTypeAndSocialId(UserAuthType type, String socialId) {

        Optional<UserAuth> userAuthOptional = userAuthRepository.findUserAuthByTypeAndId(type, socialId);

        if (userAuthOptional.isEmpty())
            return null;

        return userAuthOptional.get().getUser();
    }

    public String getKakaoSocialId(String accessToken) {

        ResponseEntity<KakaoClient.ResUserByAccessToken> response = null;
        try {
            response = kakaoClient.getUserMeByAccessToken("Bearer " + accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response == null
                || !response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null
                || response.getBody().getAppId() == null
                || !response.getBody().getAppId().equals(kakaoAppId)) {
            return null;
        }

        return response.getBody().getId().toString(); // kakao user id
    }

//    private String getFacebookSocialId(UserAuth userAuth) {
//        final String accessToken = userAuth.getId();
//
//        final FacebookRestClient.FacebookUserInfo facebookUserInfo = facebookClient.getUserInfo(
//                accessToken,
//                facebookAppId,
//                facebookSecret
//        );
//
//        if(facebookUserInfo == null || StringUtils.isEmpty(facebookUserInfo.getId()))
//            return null;
//
//        return facebookUserInfo.getId();
//    }

//    private String getNaverSocialId(UserAuth userAuth) {
//        return null; // TODO
//    }

//    private String getGoogleSocialId(UserAuth userAuth) {
//        return null; // TODO
//    }
}
