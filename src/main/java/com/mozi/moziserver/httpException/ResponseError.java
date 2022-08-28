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
        ALREADY_EXISTS_EMAIL("already exists email"),
        INVALID_EMAIL_OR_PASSWORD("invalid email or password"),
        INVALID_SEQ("invalid seq"),
        ALREADY_CREATED( "already created"),
        ALREADY_DELETED("already deleted"),
        ALREADY_EXISTS_USER_CHALLENGE_IN_PROGRESS("already exists user challenge in progress"),
        PLAN_DATE_TYPE_NOT_INVALID("plan date type cannot be in complete or fail, when creating a user-challenge"),
        ALREADY_ENDED_USER_CHALLENGE("already ended user-challenge cannot be modified"),
        PAST_DATES_UPDATE_NOT_INVALID("past dates update not invalid"),
        PAST_START_DATE("past start date not invalid"),
        INVALID_TURN("invalid turn"),
        INVALID_DATE("invalid date"),
        USER_IS_DELETED("user is deleted"),
        INVALID_USER("invalid user"),
        INVALID_IMAGE("invalid image"),
        INVALID_TOKEN("invalid token"),
        ALREADY_EXISTS_NICKNAME("already exists nickname"),
        SOCIAL_LOGIN_USER("social login user"),
        INVALID_USER_CHALLENGE("invalid user challenge"),
        INVALID_NAME("invalid name"),
        ALREADY_EXISTS_ANIMAL_NAME("already exists animal name"),
        INVALID_USER_ISLAND_OPEN("invalid user island open"),
        ALREADY_EXISTS_CONFIRM_LIKE("already exists confirm like"),
        ALREADY_CHECKED_BOARD("already checked board"),
        TODAY_ENDED_CHALLENGE("today ended challenge"),
        ALREADY_CHECKED_EMAIL("already checked email"),
        INVALID_ID_OR_PASSWORD("invalid id or password"),
        INVALID_CONFIRM("invalid confirm"),
        TODAY_STOPPED_CHALLENGE("today stopped challenge"),
        ALREADY_STOPPED_USER_CHALLENGE("already stopped user-challenge cannot be modified");


        // INVALID 는 한가지로 명확하지 않을때 쓴다.

//        BAD_REQUEST(HttpStatus.BAD_REQUEST, "bad request"),
//        INVALID_ID(HttpStatus.BAD_REQUEST, "invalid id"),
//        INVALID_SEQ(HttpStatus.BAD_REQUEST, "invalid seq"),
//        EXISTS_ID(HttpStatus.BAD_REQUEST, "exists email"),
//        DUPLICATE_USER_NICK(HttpStatus.BAD_REQUEST, "user nick exists"),
//        INVALID_QUERY(HttpStatus.BAD_REQUEST, "invalid query"),
//        INVALID_PAGE(HttpStatus.BAD_REQUEST, "invalid page"),
//        INVALID_PAGE_SIZE(HttpStatus.BAD_REQUEST, "invalid pageSize"),
//        INVALID_PREV_LAST_SEQ(HttpStatus.BAD_REQUEST, "invalid prev last seq"),
//        ALREADY_FRIEND(HttpStatus.BAD_REQUEST, "already we are friend"),
//        ALREADY_ACCEPTED(HttpStatus.BAD_REQUEST, "already accepted"),
//        ALREADY_DENIED(HttpStatus.BAD_REQUEST, "already denied"),
//        ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "already canceled"),
//        ALREADY_CREATED(HttpStatus.BAD_REQUEST, "already created"),
//        ALREADY_DELETED(HttpStatus.BAD_REQUEST, "already deleted"),
//        INVALID_TITLE(HttpStatus.BAD_REQUEST, "invalid title"),
//        INVALID_CONTENT(HttpStatus.BAD_REQUEST, "invalid content"),
//        INVALID_WANTED_DT(HttpStatus.BAD_REQUEST, "invalid wanted dt"),
//        INVALID_FRIEND_SEQ(HttpStatus.BAD_REQUEST, "invalid friend seq"),
//        INVALID_GROUP_SEQ(HttpStatus.BAD_REQUEST, "invalid group seq"),
//        INVALID_PLACE_NAME(HttpStatus.BAD_REQUEST, "invalid place name"),
//        INVALID_PICTURE_URL(HttpStatus.BAD_REQUEST, "invalid picture url"),
//        INVALID_PICTURE_URL_PREFIX(HttpStatus.BAD_REQUEST, "invalid picture url prefix"),
//        NOT_EXISTS_PICTURE_URL(HttpStatus.BAD_REQUEST, "not exists picture url"),
//        INVALID_GROUP_MEMBER_SEQ(HttpStatus.BAD_REQUEST, "invalid group member seq (not friend)"),
//        INVALID_KING_USER_SEQ(HttpStatus.BAD_REQUEST, "invalid king user seq (not friend)"),
//
//        UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "need to login"),
//
//        FORBIDDEN(HttpStatus.FORBIDDEN, "need to set nick"),
//        NO_AUTHORITY(HttpStatus.FORBIDDEN, "no authority"),
//        ACCESS_DENIED_DIARY(HttpStatus.FORBIDDEN, "access denied diary"),
//        PERMISSION_DENIED_DIARY(HttpStatus.FORBIDDEN, "permission denied diary"),
//        ACCESS_DENIED_GROUP(HttpStatus.FORBIDDEN, "access denied group"),
//        PERMISSION_DENIED_GROUP(HttpStatus.FORBIDDEN, "permission denied group"),
//        LEAVE_DENIED_GROUP(HttpStatus.FORBIDDEN, "leave denied group (king)"),
//
//        USER_NOT_EXISTS(HttpStatus.NOT_FOUND, "user not exists"),
//        FRIEND_NOT_EXISTS(HttpStatus.NOT_FOUND, "friend not exists"),
//        DIARY_NOT_EXISTS(HttpStatus.NOT_FOUND, "diary not exists"),
//        GROUP_NOT_EXISTS(HttpStatus.NOT_FOUND, "group not exists"),
//        GROUP_MEMBER_NOT_EXISTS(HttpStatus.NOT_FOUND, "group member not exists"),
//
//        ALREADY_UPDATED(HttpStatus.CONFLICT, "already update"),
//        ALREADY_GROUP_MEMBER(HttpStatus.CONFLICT, "already group member"),
//
//        EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "external api error"),
//        TODO_CODE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "critical error"),
//        UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "unexcepected error");

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
    // 로그인은 했지만 권한이 없음
    @RequiredArgsConstructor
    enum Forbidden {
        NO_AUTHORITY("no authority"),
        NEED_NICK("need to set nick");

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
        USER_NOT_EXISTS("user not exists"),
        CHALLENGE_NOT_EXISTS("challenge not exists"),
        USER_CHALLENGE_NOT_EXISTS("user-challenge not exists"),
        USER_STICKER_NOT_EXISTS("user sticker not exists"),
        EMAIL_NOT_EXISTS("email not exists"),
        NICKNAME_NOT_EXISTS("nickname not exists"),
        POSTBOX_MESSAGE_ADMIN_NOT_EXISTS("postbox message admin not exists"),
        POSTBOX_MESSAGE_ANIMAL_NOT_EXISTS("postbox message animal not exists"),
        ANIMAL_NOT_EXISTS("animal not exists"),
        STICKER_NOT_EXISTS("sticker not exists"),

        ISLAND_NOT_EXISTS("island not exists"),

        ISLAND_IMG_NOT_EXISTS("islandImg not exists"),

        PREPARATIONITEM_NOT_EXISTS("preparationItem not exists"),
        CONFIRM_NOT_EXISTS("confirm not exists"),
        CONFIRM_LIKE_NOT_EXISTS("confirm like not exists"),
        BOARD_NOT_EXISTS("board not exists");

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