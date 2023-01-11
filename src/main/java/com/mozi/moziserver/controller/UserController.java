package com.mozi.moziserver.controller;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.entity.UserReward;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.model.req.*;
import com.mozi.moziserver.model.res.ResEmail;
import com.mozi.moziserver.model.res.ResUserPoint;
import com.mozi.moziserver.repository.UserRepository;
import com.mozi.moziserver.security.RememberMeService;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.EmailAuthService;
import com.mozi.moziserver.service.UserRewardService;
import com.mozi.moziserver.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RememberMeService rememberMeService;
    private final EmailAuthService emailAuthService;
    private final UserRewardService userRewardService;

    @ApiOperation("가입 (ID)")
    @PostMapping(value = "/v1/users/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> signUpUser(
            @RequestBody @Valid ReqUserSignUp reqUserSignUp
    ) {
        userService.signUp(reqUserSignUp);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("로그인 (ID, 소셜)")
    @PostMapping(value = "/v1/users/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> signInUser(
            @RequestBody @Valid ReqUserSignIn req,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) {
        if (req.getType() == UserAuthType.EMAIL && !StringUtils.hasLength(req.getPw())) {
            throw ResponseError.BadRequest.INVALID_EMAIL_OR_PASSWORD.getResponseException();
        }

        Authentication auth = userService.signIn(req);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        rememberMeService.loginSuccess(request, response, auth);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("로그아웃")
    @PostMapping("/v1/users/signout")
    public ResponseEntity<Object> signOut() {
        throw new IllegalStateException("This Method not working");
    }

    @ApiOperation("닉네임 중복 확인")
    @GetMapping("/v1/users/nicknames/{nickName}/check")
    public ResponseEntity<Object> checkNickNameDuplicate(
            @PathVariable String nickName
    ) {
        return new ResponseEntity<>(
                userService.checkNickNameDuplicate(nickName) ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @ApiOperation("이메일 찾기")
    @GetMapping("/v1/find/emails")
    public ResEmail findUserAuthEmail(
            @Valid ReqUserNickNameAndPw req
    ) {
        UserAuth userAuth = userService.findUserAuthByNicknameAndPw(req.getNickName(), req.getPw());

        if (userAuth.getType() != UserAuthType.EMAIL) {
            throw ResponseError.BadRequest.SOCIAL_LOGIN_USER.getResponseException();
        }

        return ResEmail.of(userAuth);
    }

    @ApiOperation("이메일 중복 확인")
    @GetMapping("/v1/users/emails/{email}/check")
    public ResponseEntity<Object> checkEmailDuplicate(
            @PathVariable String email
    ) {
        return new ResponseEntity<>(
                userService.checkEmailDuplicate(email) ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @ApiOperation("비밀번호 재설정 (이메일 인증 필수)")
    @PostMapping("/v1/users/find-pw")
    public ResponseEntity<Object> sendPwEmail(
            @RequestBody @Valid ReqAuthEmail req
    ) {
        UserAuth userAuth = userService.findUserAuthByTypeAndId(UserAuthType.EMAIL, req.getEmail())
                .orElseThrow(ResponseError.NotFound.EMAIL_NOT_EXISTS::getResponseException);

        emailAuthService.sendResetPwEmail(userAuth);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("FCM 토큰 등록")
    @PostMapping("/v1/users/me/fcm")
    public ResponseEntity<Object> updateFcmToken(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqFcmToken reqFcmToken
    ) {
        if (!userSeq.equals(userSeq)) {
            throw ResponseError.Forbidden.NO_AUTHORITY.getResponseException();
        }

        User user = userService.getUserBySeq(userSeq)
                .orElseThrow(ResponseError.InternalServerError.UNEXPECTED_ERROR::getResponseException);

        userService.upsertUserFcm(user, reqFcmToken.getDeviceId(), reqFcmToken.getToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("내 포인트 조회")
    @GetMapping("/v1/users/me/point")
    public ResUserPoint getUserPoint(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        UserReward userReward = userRewardService.getUserReward(userSeq);
        return ResUserPoint.of(userReward);
    }
}
