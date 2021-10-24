package com.mozi.moziserver.service;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.model.req.ReqUserSignIn;
import com.mozi.moziserver.model.req.ReqUserSignUp;
import com.mozi.moziserver.repository.UserAuthRepository;
import com.mozi.moziserver.repository.UserRepository;
import com.mozi.moziserver.security.UserAuthToken;
import com.mozi.moziserver.security.UserAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignService {

    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final AuthenticationManager authenticationManager;
    private final UserAuthenticationProvider userAuthenticationProvider;
//    private final KakaoClient kakaoClient;

    @Value("${social.kakao.appid}")
    Long kakaoAppId;
    @Value("${social.kakao.adminkey}")
    String kakaoAdminKey;

//    @Value("${social.facebook.appid}")
//    String facebookAppId;
//    @Value("${social.facebook.secret}")
//    String facebookSecret;

    public User signUp(ReqUserSignUp reqUserSignUp) {

        if (reqUserSignUp.getType() != UserAuthType.ID) {
            throw ResponseError.BadRequest.INVALID_ID.getResponseException();
        }

        String email = reqUserSignUp.getId();

        if (!email.matches(Constant.ID_REGEX)) {
            throw ResponseError.BadRequest.INVALID_ID.getResponseException();
        }

        UserAuth userAuth = new UserAuth();
        userAuth.setType(UserAuthType.ID);

        User user = new User();
        userRepository.save(user);

        if (user.getSeq() == null)
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();

        userAuth.setId(reqUserSignUp.getId());
        userAuth.setPw(reqUserSignUp.getPw());
        userAuth.setUser(user);

        // TODO 휴대폰인증번호 확인
        try {
            userAuthRepository.save(userAuth);
        } catch (Exception e) {
            e.printStackTrace();
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException(); // FIXME id 중복 에러체크
        }

        if (userAuth.getSeq() == null)
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();

        return user;
    }

    public Authentication signIn(ReqUserSignIn reqUserSignIn) {

        UserAuthToken userAuthToken = new UserAuthToken(reqUserSignIn);

        Authentication auth = authenticationManager.authenticate(userAuthToken);

        if ((auth == null || !auth.isAuthenticated()) && reqUserSignIn.getType().isSocial()) {
//            if (reqUserSignIn.getType() == UserAuthType.KAKAO) kakaoSignUp(reqUserSignIn);
//            else if(reqUserSignIn.getType() == UserAuthType.FACEBOOK) facebookSignUp(reqUserSignIn);
//            else if(reqUserSignIn.getType() == UserAuthType.NAVER) naverSignUp(reqUserSignIn);
//            else if(reqUserSignIn.getType() == UserAuthType.GOOGLE) googleSignUp(reqUserSignIn);

            auth = authenticationManager.authenticate(userAuthToken);
        }

        if (auth == null || !auth.isAuthenticated()) {
            throw ResponseError.BadRequest.BAD_REQUEST.getResponseException();
        }

        return auth;
    }

    private void kakaoSignUp(ReqUserSignIn reqUserSignIn) {
        final String accessToken = reqUserSignIn.getId();

        final String kakaoSocialId = userAuthenticationProvider.getKakaoSocialId(accessToken);

        if (kakaoSocialId == null)
            return;

        User user = new User();
        userRepository.save(user);

        if (user.getSeq() == null)
            return;

        UserAuth userAuth = new UserAuth();
        userAuth.setType(UserAuthType.KAKAO);
        userAuth.setId(kakaoSocialId);
        userAuth.setUser(user);

        userAuthRepository.save(userAuth);
    }

    private void facebookSignUp(ReqUserSignIn reqUserSignIn) {
//        final String accessToken = reqUserSignIn.getId();
//
//        final FacebookRestClient.FacebookUserInfo facebookUserInfo =
//                facebookClient.getUserInfo(accessToken, facebookAppId, facebookSecret);
//
//        if(facebookUserInfo == null || StringUtils.isEmpty(facebookUserInfo.getId()))
//            return;
//
//        String name = facebookUserInfo.getName();
//
//        User user = new User();
//        int insertCount = userRepository.insertUser(user);
//
//        if(user.getSeq() == null)
//            return;
//
//        UserAuth userAuth = new UserAuth();
//        userAuth.setType(UserAuthType.FACEBOOK);
//        userAuth.setId(facebookUserInfo.getId());
//        userAuth.setUserSeq(user.getSeq());
//
//        userAuthRepository.insertUserAuth(userAuth);
    }

    private void naverSignUp(ReqUserSignIn reqUserSignIn) {
        // TODO
    }

    private void googleSignUp(ReqUserSignIn reqUserSignIn) {
        // TODO
    }
}
