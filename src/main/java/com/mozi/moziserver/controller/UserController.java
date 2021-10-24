package com.mozi.moziserver.controller;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.req.ReqUserSignIn;
import com.mozi.moziserver.model.req.ReqUserSignUp;
import com.mozi.moziserver.service.UserSignService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserSignService userSignService;

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
        userSignService.signUp(reqUserSignUp);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @SwaggerResponseError({
//            ResponseError.BAD_REQUEST
//    })
    @ApiOperation("로그인 (ID, 소셜)")
    @PostMapping(value = "/v1/users/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signInUser(
            @RequestBody @Valid ReqUserSignIn reqUserSignIn,
            HttpSession session
    ) {

        // TODO Param Validation

        Authentication auth = userSignService.signIn(reqUserSignIn);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        MDC.put(Constant.MDC_KEY_USER_SEQ, ((User) auth.getPrincipal()).getSeq().toString());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @SwaggerResponseError({})
    @ApiOperation("로그아웃")
    @PostMapping("/v1/users/signout")
    public ResponseEntity<Void> signOut() {
        throw new IllegalStateException("This Method not working");
    }
}
