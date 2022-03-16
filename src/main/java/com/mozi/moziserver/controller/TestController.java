package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.req.ReqTestUserChallengeStartDateUpdate;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.UserChallengeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Profile("!production")
@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final UserChallengeService userChallengeService;

    @ApiOperation("유저 챌린지 startDate 변경")
    @PutMapping("/user-challenges/{seq}")
    public ResponseEntity<Void> updateUserChallengeStartDate(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long userChallengeSeq,
            @RequestBody @Valid ReqTestUserChallengeStartDateUpdate req
    ) {
        userChallengeService.updateUserChallengeStartDate(userSeq, userChallengeSeq, req.getStartDate());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}