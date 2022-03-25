package com.mozi.moziserver.controller;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.model.req.*;
import com.mozi.moziserver.model.res.ResConfirmList;
import com.mozi.moziserver.model.res.ResDuplicationCheck;
import com.mozi.moziserver.model.res.ResEmail;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.EmailAuthService;
import com.mozi.moziserver.service.MyPageService;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MyPageService myPageService;
    private final EmailAuthService emailAuthService;

    //
//    @SwaggerResponseError({
//            ResponseError.INVALID_ID,
//            ResponseError.EXISTS_ID,
//            ResponseError.UNEXPECTED_ERROR
//    })
    @ApiOperation("가입 (ID)")
    @PostMapping(value = "/v1/users/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signUpUser(
            @RequestBody @Valid ReqUserSignUp reqUserSignUp
    ) {
        userService.signUp(reqUserSignUp);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @SwaggerResponseError({
//            ResponseError.BAD_REQUEST
//    })
    @ApiOperation("로그인 (ID, 소셜)")
    @PostMapping(value = "/v1/users/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signInUser(
            @RequestBody @Valid ReqUserSignIn req,
            HttpSession session
    ) {
        if (req.getType() == UserAuthType.EMAIL && !StringUtils.hasLength(req.getPw())) {
            throw ResponseError.BadRequest.INVALID_EMAIL_OR_PASSWORD.getResponseException();
        }

        Authentication auth = userService.signIn(req);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @SwaggerResponseError({})
    @ApiOperation("로그아웃")
    @PostMapping("/v1/users/signout")
    public ResponseEntity<Void> signOut() {
        throw new IllegalStateException("This Method not working");
    }


    //닉네임 확인
    @ApiOperation("이메일 찾기_닉네임 확인")
    @PostMapping("/v1/users/nicknames")
    public Boolean isUserNickName(
            @RequestBody @Valid ReqNickName req
    ) {
        User user=userService.findUserByNickName(req.getNickName());
        if(user!=null) return Boolean.TRUE;
        return Boolean.FALSE;
    }


    @ApiOperation("이메일 찾기_이메일 보여주기")
    @PostMapping("/v1/users/user-auths/emails")
    public ResEmail findUserAuthEmail(
        @RequestBody @Valid ReqUserNickNameAndPw req
    ){
        UserAuth userAuth=userService.findUserEmail(req.getNickName(),req.getPw());

        return ResEmail.of(userAuth);

    }

    @ApiOperation("비밀번호 찾기(재설정)_이메일 확인")
    @PostMapping("/v1/users/user-auths")
    public ResponseEntity<Void> checkUserAuth(
            @RequestBody @Valid ReqAuthEmail req
    ) {
        return userService.findUserAuth(req.getEmail());
    }

    @ApiOperation("비밀번호 찾기(재설정)_인증 메일 전송")
    @PostMapping("/v1/users/email-check-auths")
    public ResponseEntity<String> sendPwEmail(
            @RequestBody @Valid ReqAuthEmail req
    ){
        return userService.sendEmail(req.getEmail());
    }

    @ApiOperation("비밀번호 찾기(재설정)_이메일 인증 확인")
    @GetMapping("/v1/users/email-check-auths/{token}")
    public ResponseEntity<Long> checkEmailAuth(
            @PathVariable String token
    ) {
        return emailAuthService.findUsedDt(token);
    }

    @ApiOperation("비밀번호 찾기(재설정)_비밀번호 수정")
    @PutMapping("/v1/users/email-check-auths/user-auths")
    public ResponseEntity<Void> updateUserPasswordToEmail(
            @RequestBody @Valid ReqUserSeqAndPw req
    ) {
        myPageService.updateUserPassword(req.getUserSeq(),req.getPw());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
