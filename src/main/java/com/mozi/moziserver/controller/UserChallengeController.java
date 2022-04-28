package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.model.req.ReqUserChallengeCreate;
import com.mozi.moziserver.model.req.ReqUserChallengeList;
import com.mozi.moziserver.model.res.ResUserChallengeList;
import com.mozi.moziserver.repository.ChallengeRepository;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.UserChallengeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserChallengeController {
    private final UserChallengeService userChallengeService;
    private final ChallengeRepository challengeRepository;

    @ApiOperation("유저 챌린지 날짜별 리스트 조회")
    @GetMapping("/v1/period/user-challenges")
    public List<ResUserChallengeList> getUserChallengeList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqUserChallengeList req
    ) {
        return userChallengeService.getUserChallengeList(userSeq, req)
                .stream()
                .map(ResUserChallengeList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("유저 챌린지 전체 조회")
    @GetMapping("/v1/user-challenges")
    public List<ResUserChallengeList> getUserChallengeList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {
        return userChallengeService.getUserChallengeList(userSeq, req)
                .stream()
                .map(ResUserChallengeList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("유저 챌린지 생성")
    @PostMapping("/v1/user-challenges")
    public ResponseEntity<Void> createUserChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqUserChallengeCreate req
    ) {
        userChallengeService.createUserChallenge(userSeq, req);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("유저 챌린지 그만두기")
    @PutMapping("/v1/user-challenges/{seq}/quit")
    public ResponseEntity<Void> quitUserChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long userChallengeSeq
    ) {
        userChallengeService.quitUserChallenge(userSeq, userChallengeSeq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
