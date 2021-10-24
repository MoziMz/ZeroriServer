package com.mozi.moziserver.security;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.httpException.ResponseException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        final ResponseException e = ResponseError.Unauthorized.UNAUTHORIZED.getResponseException();
        response.sendError(
                e.getHttpStatus().value(),
                e.getMessage());
    }
}