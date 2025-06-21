package com.mozi.moziserver.security;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.httpException.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException,
            ServletException {
        if (!response.isCommitted()) {
            final ResponseException e = ResponseError.Forbidden.NEED_NICK.getResponseException();
            response.sendError(
                    e.getHttpStatus().value(),
                    e.getMessage());
        }
    }
}