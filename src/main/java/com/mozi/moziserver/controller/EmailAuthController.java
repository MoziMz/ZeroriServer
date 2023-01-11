package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.EmailAuthResult;
import com.mozi.moziserver.service.EmailAuthService;
import io.swagger.annotations.ApiOperation;
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
    public ResponseEntity<Object> authEmail(@PathVariable String token) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();

        EmailAuthResult result = emailAuthService.authEmail(token);

        switch (result.getStatus()) {
            case SUCC:
                switch (result.getType()) {
                    case JOIN:
                        httpHeaders.setLocation(new URI("/email-auth-join-valid.html"));
                        break;
                    case RESET_PW:
                        httpHeaders.setLocation(new URI("/email-auth-reset-pw-valid.html"));
                        break;
                        // TODO ?JSESSIONID=
                    case RESET_EMAIL:
                        httpHeaders.setLocation(new URI("/email-auth-reset-email-valid.html"));
                        break;
                }
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
