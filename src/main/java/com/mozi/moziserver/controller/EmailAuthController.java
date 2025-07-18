package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.EmailAuth;
import com.mozi.moziserver.service.EmailAuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Hidden
@RestController
@RequestMapping("/email/auth")
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    @Operation(summary = "", hidden = true)
    @GetMapping("/{token}")
    public ResponseEntity<Object> authEmail(@PathVariable String token) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();

        EmailAuth emailAuth= emailAuthService.authEmail(token);

        switch (emailAuth.getEmailAuthResultState()) {
            case SUCCESS:
                switch (emailAuth.getType()) {
                    case JOIN:
                        httpHeaders.setLocation(new URI("/email/email-auth-join-valid.html"));
                        break;
                    case RESET_PW:
                        httpHeaders.setLocation(new URI("/email/email-auth-reset-pw-valid.html"));
                        break;
                        // TODO ?JSESSIONID=
                    case RESET_EMAIL:
                        httpHeaders.setLocation(new URI("/email/email-auth-reset-email-valid.html"));
                        break;
                }
                break;
            case ALREADY_SUCCESS:
                httpHeaders.setLocation(new URI("/email/email-auth-already-valid.html"));
                break;
            default:
                httpHeaders.setLocation(new URI("/email/email-auth-invalid.html"));
                break;
        }

        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
