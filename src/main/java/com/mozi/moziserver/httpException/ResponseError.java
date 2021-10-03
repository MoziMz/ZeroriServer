package com.mozi.moziserver.httpException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public interface ResponseError {

    //400: Bad request
    @RequiredArgsConstructor
    enum BadRequest {
        METHOD_ARGUMENT_NOT_VALID(""),
        BAD_REQUEST("bad request"),
        INVALID_EMAIL("invalid email"),
        ALREADY_EXISTS_EMAIL("already exists email");

        private final String message;

        public ResponseException getResponseException() {
            return getResponseException(null);
        }

        public ResponseException getResponseException(String appendMessage) {
            return new ResponseException(HttpStatus.BAD_REQUEST, name(),
                    appendMessage != null ? message + "(" + appendMessage + ")" : message);
        }
    }

    //401: Need to login
    @RequiredArgsConstructor
    enum Unauthorized {
        UNAUTHORIZED("need to login");

        private final String message;

        public ResponseException getResponseException() {
            return getResponseException(null);
        }

        public ResponseException getResponseException(String appendMessage) {
            return new ResponseException(HttpStatus.UNAUTHORIZED, name(),
                    appendMessage != null ? message + "(" + appendMessage + ")" : message);
        }
    }

    //403: Be authenticated but has not authorization
    @RequiredArgsConstructor
    enum Forbidden {
        NO_AUTHORITY("no authority");

        private final String message;

        public ResponseException getResponseException() {
            return getResponseException(null);
        }

        public ResponseException getResponseException(String appendMessage) {
            return new ResponseException(HttpStatus.FORBIDDEN, name(),
                    appendMessage != null ? message + "(" + appendMessage + ")" : message);
        }
    }

    //404
    @RequiredArgsConstructor
    enum NotFound {
        USER_NOT_EXISTS("user not exists");

        private final String message;

        public ResponseException getResponseException() {
            return getResponseException(null);
        }

        public ResponseException getResponseException(String appendMessage) {
            return new ResponseException(HttpStatus.NOT_FOUND, name(),
                    appendMessage != null ? message + "(" + appendMessage + ")" : message);
        }
    }

    //409: Simultaneous access
    @RequiredArgsConstructor
    enum Conflict {
        ALREADY_UPDATED("already update");

        private final String message;

        public ResponseException getResponseException() {
            return getResponseException(null);
        }

        public ResponseException getResponseException(String appendMessage) {
            return new ResponseException(HttpStatus.CONFLICT, name(),
                    appendMessage != null ? message + "(" + appendMessage + ")" : message);
        }
    }

    //500: Server problem
    @RequiredArgsConstructor
    enum InternalServerError {
        UNEXPECTED_ERROR("unexcepected error");

        private final String message;

        public ResponseException getResponseException() {
            return getResponseException(null);
        }

        public ResponseException getResponseException(String appendMessage) {
            return new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, name(),
                    appendMessage != null ? message + "(" + appendMessage + ")" : message);
        }
    }
}