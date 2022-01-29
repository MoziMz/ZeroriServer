package com.mozi.moziserver.httpException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
@SuppressWarnings("unused")
public class ResponseExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ResponseException> handle(ResponseException e) {
        return new ResponseEntity<>(e, e.getHttpStatus());
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ResponseException> handle(MethodArgumentNotValidException e) {
        final String message = e.getFieldError() != null
                ? e.getFieldError().getDefaultMessage() + " (" + e.getFieldError().getField() + ")"
                : e.getGlobalError() != null ? e.getGlobalError().getDefaultMessage() : e.getMessage();
        ResponseException ex = ResponseError.BadRequest.METHOD_ARGUMENT_NOT_VALID.getResponseException(message);
        return handle(ex);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ResponseException> handle(BadCredentialsException e) {
        ResponseException ex = ResponseError.BadRequest.INVALID_EMAIL_OR_PASSWORD.getResponseException();
        return handle(ex);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ResponseException> handle(Throwable t) {
        log.error("UNEXPECTED_ERROR", t);

        ResponseException ex = ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException(t.getMessage());
        return handle(ex);
    }
}