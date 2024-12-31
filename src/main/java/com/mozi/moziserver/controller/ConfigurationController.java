package com.mozi.moziserver.controller;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Board;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.mappedenum.QuestionCategoryType;
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
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConfigurationController {

    private final QuestionService questionService;
    private final BoardService boardService;
    private final SuggestionService suggestionService;
    private final UserService userService;

    // -------------------- -------------------- Question -------------------- -------------------- //
    @ApiOperation("문의 등록")
    @PostMapping("/v1/questions")
    public ResponseEntity<Object> createQuestion(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestParam(name = "email", required = true) String email,
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "content", required = true) String content,
            @RequestParam(name = "questionCategory", required = true) QuestionCategoryType category,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) {

        User user = userService.getUserBySeq(userSeq);

        // TODO FIX ME
        ReqQuestionCreate reqQuestionCreate = new ReqQuestionCreate();
        reqQuestionCreate.setEmail(email);
        reqQuestionCreate.setTitle(title);
        reqQuestionCreate.setContent(content);
        reqQuestionCreate.setQuestionCategory(category);
        reqQuestionCreate.setImage(image);
        questionService.createQuestion(user, reqQuestionCreate);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- Board -------------------- -------------------- //
    @ApiOperation("공지사항 보기")
    @GetMapping("/v1/boards")
    public List<Board> getBoardListByCreatedAt(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {

        User user = userService.getUserBySeq(userSeq);
        return boardService.getAllBoardListByCreatedAt(user, req);
    }

    @ApiOperation("공지사항 확인 완료")
    @PutMapping("/v1/boards/{seq}/checked")
    public ResponseEntity<Object> checkedBoard(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq
    ) {

        User user = userService.getUserBySeq(userSeq);
        boardService.checkBoard(user, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- Suggestion -------------------- -------------------- //
    @ApiOperation("챌린지 제안하기")
    @PostMapping("/v1/suggestions")
    public ResponseEntity<Object> createSuggestion(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqSuggestionCreate req
    ) {

        User user = userService.getUserBySeq(userSeq);
        suggestionService.createSuggestion(user, req);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- User -------------------- -------------------- //
    @ApiOperation("내 정보 조회")
    @GetMapping("/v1/users/me")
    public ResUserInfo getUserInfo(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {

        User user = userService.getUserBySeq(userSeq);

        return ResUserInfo.of(user);
    }

    @ApiOperation("닉네임 등록 및 수정")
    @PostMapping("/v1/users/me/nickname/{nickName}")
    public ResponseEntity<Object> updateUserNickName(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable String nickName
    ) {

        User user = userService.getUserBySeq(userSeq);

        userService.updateNickname(user, nickName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("튜토리얼 확인 완료")
    @PutMapping("/v1/users/me/tutorial/checked")
    public ResponseEntity<Object> checkedTutorial(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        userService.checkTutorial(userSeq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("비밀번호 확인")
    @PostMapping("/v1/users/me/password/check")
    public ResponseEntity<Object> checkPassword(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqUserPw req
    ) {

        User user = userService.getUserBySeq(userSeq);

        if (!userService.checkPassword(user, req.getCurrentPw())) {
            throw ResponseError.BadRequest.NOT_MATCH_AN_EXISTING_PASSWORD.getResponseException();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("비밀번호 재설정 (이메일 인증 X)")
    @PutMapping("/v1/users/me/password")
    public ResponseEntity<Object> updateUserPassword(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqUserPw req
            ) {
        User user = userService.getUserBySeq(userSeq);

        userService.updatePw(user, req.getNewPw(), req.getCurrentPw());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("이메일 변경 (이메일 인증 필수)")
    @PutMapping(value = "/v1/users/me/email/{email}")
    public ResponseEntity<Object> authUserEmail(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable @Valid String email
    ) {
        User user = userService.getUserBySeq(userSeq);

        userService.updateEmail(user, email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("탈퇴하기")
    @PostMapping(value = "v1/users/resign")
    public ResponseEntity<Object> resign(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqResign reqResign,
            HttpSession session
    ) {
        User user = userService.getUserBySeq(userSeq);
        // 세션에서 정보 지우기 -> 로그아웃 참고
        userService.resignUser(user, reqResign);
        session.invalidate();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
