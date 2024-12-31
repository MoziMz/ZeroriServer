package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.req.ReqTestUserChallengeStartDateUpdate;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.BadWordService;
import com.mozi.moziserver.service.UserChallengeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Profile("!production")
@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final UserChallengeService userChallengeService;
    private final BadWordService badWordService;

    @ApiOperation("유저 챌린지 startDate 변경")
    @PutMapping("/user-challenges/{seq}")
    public ResponseEntity<Object> updateUserChallengeStartDate(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long userChallengeSeq,
            @RequestBody @Valid ReqTestUserChallengeStartDateUpdate req
    ) {

        userChallengeService.updateUserChallengeStartDate(userSeq, userChallengeSeq, req.getStartDate());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("비속어 생성")
    @PostMapping("/admin/badwords")
    public ResponseEntity<Object> createBadWord(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestParam String content
    ) {

        badWordService.createBadword(content);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
