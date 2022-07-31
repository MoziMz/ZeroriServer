package com.mozi.moziserver.controller;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Board;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.req.*;
import com.mozi.moziserver.model.res.ResUserInfo;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConfigurationController {

    private final QuestionService questionService;
    private final BoardService boardService;
    private final SuggestionService suggestionService;
    private final UserService userService;

    @ApiOperation("문의 등록")
    @PostMapping("/v1/questions")
    public ResponseEntity<Void> createQuestion(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqQuestionCreate reqQuestionCreate
    ) {
        questionService.createQuestion(userSeq, reqQuestionCreate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("공지사항 보기")
    @GetMapping("/v1/boards")
    public List<Board> getBoardListByCreatedAt(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {
        return boardService.getAllBoardListByCreatedAt(userSeq, req);
    }

    @ApiOperation("공지사항 확인 완료")
    @PutMapping("/v1/boards/{seq}/checked")
    public ResponseEntity<Void> checkedBoard(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq
    ) {
        boardService.checkBoard(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 제안하기")
    @PostMapping("/v1/suggestions")
    public ResponseEntity<Void> createSuggestion(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqSuggestionCreate reqSuggestionCreate
    ) {
        suggestionService.createSuggestion(userSeq, reqSuggestionCreate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("내 정보 조회")
    @GetMapping("/v1/users/me")
    public ResUserInfo getUserInfo(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        User user = userService.getUserBySeq(userSeq)
                .orElseThrow(ResponseError.InternalServerError.UNEXPECTED_ERROR::getResponseException);

        return ResUserInfo.of(user);
    }

    @ApiOperation("닉네임 등록 및 수정")
    @PostMapping("/v1/users/me/nickname/{nickName}")
    public ResponseEntity<Void> updateUserNickName(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable String nickName
    ) {
        User user = userService.getUserBySeq(userSeq)
                .orElseThrow(ResponseError.InternalServerError.UNEXPECTED_ERROR::getResponseException);

        userService.updateNickname(user, nickName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("비밀번호 재설정 (이메일 인증 X)")
    @PutMapping("/v1/users/me/password")
    public ResponseEntity<Void> updateUserPassword(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqUserPw req
            ) {
        User user = userService.getUserBySeq(userSeq)
                .orElseThrow(ResponseError.InternalServerError.UNEXPECTED_ERROR::getResponseException);

        userService.updatePw(user, req.getPw());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("이메일 변경 (이메일 인증 필수)")
    @PutMapping(value = "/v1/users/me/email/{email}")
    public ResponseEntity<Void> authUserEmail(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable @Valid String email
    ) {
        User user = userService.getUserBySeq(userSeq)
                .orElseThrow(ResponseError.InternalServerError.UNEXPECTED_ERROR::getResponseException);

        userService.updateEmail(user, email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("탈퇴하기")
    @PostMapping(value = "v1/users/resign")
    public ResponseEntity<Void> resign(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqResign reqResign,
            HttpSession session
    ) {
        User user = userService.getUserBySeq(userSeq)
                .orElseThrow(ResponseError.InternalServerError.UNEXPECTED_ERROR::getResponseException);

        // 세션에서 정보 지우기 -> 로그아웃 참고
        userService.resignUser(user, reqResign);
        session.invalidate();

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
