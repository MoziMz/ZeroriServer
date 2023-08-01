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
        ALREADY_CREATED("already created"),
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
        ALREADY_STOPPED_USER_CHALLENGE("already stopped user-challenge cannot be modified"),
        INVALID_PASSWORD("invalid password"),
        NOT_MATCH_AN_EXISTING_PASSWORD("not match an existing password"),
        INVALID_NICKNAME("invalid nick name"),
        //The nickname includes slang
        NICKNAME_WITH_BAD_WORD("nick name with bad word"),
        INVALID_START_DATE_END_DATE("start date must be earlier than or equal to end date"),
        INVALID_DECLARATION("invalid declaration"),
        INVALID_EMAIL_AUTH("email authentication is required"),
        EXPIRED_EMAIL_AUTH("expired email authentication"),
        INVALID_CONFIRM_STATE_TYPE("invalid confirm update sate type"),
        INVALID_CURRENT_TAG_LIST("invalid current tag list"),
        INVALID_CURRENT_THEME_LIST("invalid current theme list"),
        CHALLENGE_STATE_TYPE_IS_DELETED("challenge state type is deleted"),
        INVALID_ANIMAL_SEQ("invalid animal seq"),
        ALREADY_EXISTS_TOPIC_TITLE("already exists topic title"),
        TOPIC_SEQ_IS_DUPLICATED("topic seq is duplicated"),
        CHALLENGE_SEQ_IS_DUPLICATED("challenge seq is duplicated"),
        ALREADY_EXISTS_CHALLENGE_TOPIC("already exists challenge topic"),
        ALREADY_EXISTS_CHALLENGE_SEQ("already exists challenge seq");

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
        NO_MORE_USER_CHALLENGES("no more user-challenges"),
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
        BOARD_NOT_EXISTS("board not exists"),
        USER_NOTICE_NOT_EXISTS("user notice not exists"),
        EMAIL_AUTH_NOT_EXISTS("email auth not exists"),
        QUESTION_NOT_EXISTS("question not exists"),
        SUGGESTION_NOT_EXISTS("suggestion not exists"),
        CHALLENGE_THEME_NOT_EXISTS("challenge theme not exists"),
        TAG_NOT_EXISTS("tag not exists"),
        CHALLENGE_TAG_NOT_EXISTS("challenge tag not exists"),
        DETAIL_ISLAND_NOT_EXISTS("detail island not exists"),
        NOT_EXISTS("not exists"),
        ANIMAL_ITEM_NOT_EXISTS("animal item not exists"),
        CHALLENGE_TOPIC_NOT_EXISTS("challenge topic not exists");

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