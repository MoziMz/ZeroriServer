package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminUserService;
import com.mozi.moziserver.model.adminReq.ReqAdminSignIn;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminAuthenticationController {

    private final AdminUserService adminUserService;

    @Operation(summary = "관리자 로그인 (ID)")
    @PostMapping(value = "/admin/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> signInUser(
            @RequestBody @Valid ReqAdminSignIn req,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) {
        Authentication auth = adminUserService.signIn(req);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}