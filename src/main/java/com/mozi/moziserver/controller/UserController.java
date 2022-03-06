package com.mozi.moziserver.controller;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.model.req.ReqUserNickNameAndEmail;
import com.mozi.moziserver.model.req.ReqUserSignIn;
import com.mozi.moziserver.model.req.ReqUserSignUp;
import com.mozi.moziserver.service.UserService;
import io.swagger.annotations.ApiOperation;
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

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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


    @ApiOperation("이메일 찾기_닉네임, 비밀번호 사용")
    @PostMapping("/v1/users/find-email")
    public ResponseEntity<String> findEmail(
        @RequestBody @Valid ReqUserNickNameAndEmail req
    ){
        return userService.findUserEmail(req);
    }
}
