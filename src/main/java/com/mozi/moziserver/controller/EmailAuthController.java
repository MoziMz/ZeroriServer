package com.mozi.moziserver.controller;

import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.EmailAuthService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import java.net.URI;

@ApiIgnore
@RestController
@RequestMapping("/email/auth")
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    @ApiOperation(value = "", hidden = true)
    @GetMapping("/{token}")
    public ResponseEntity<Void> authEmail(@PathVariable String token) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();

        switch (emailAuthService.authEmail(token)) {
            case SUCC:
                httpHeaders.setLocation(new URI("/email-auth-valid.html"));
                break;
            case ALREADY_SUCC:
                httpHeaders.setLocation(new URI("/email-auth-already-valid.html"));
                break;
            default:
                httpHeaders.setLocation(new URI("/email-auth-invalid.html"));
                break;
        }

        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @ApiOperation(value = "", hidden = true)
    @GetMapping("/check/{token}")
    public ResponseEntity<Void> authCheckEmail(
            @PathVariable String token,
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();

        switch (emailAuthService.authCheckEmail(userSeq, token)) {
            case SUCC:
                httpHeaders.setLocation(new URI("/email-auth-valid.html"));
                break;
            case ALREADY_SUCC:
                httpHeaders.setLocation(new URI("/email-auth-already-valid.html"));
                break;
            default:
                httpHeaders.setLocation(new URI("/email-auth-invalid.html"));
                break;
        }
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @ApiOperation(value = "", hidden = true)
    @GetMapping("/pw-check/{token}")
    public ResponseEntity<Void> authCheckPwEmail(
            @PathVariable("token")String token
    ) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();

        switch (emailAuthService.authCheckPwEmail(token)) {
            case SUCC:
                httpHeaders.setLocation(new URI("/pw-email-auth-valid.html"));
                break;
            case ALREADY_SUCC:
                httpHeaders.setLocation(new URI("/email-auth-already-valid.html"));
                break;
            default:
                httpHeaders.setLocation(new URI("/email-auth-invalid.html"));
                break;
        }
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

}
