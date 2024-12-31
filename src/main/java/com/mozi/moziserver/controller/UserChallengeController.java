package com.mozi.moziserver.controller;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.req.ReqChallengeAndDate;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.model.req.ReqUserChallengeCreate;
import com.mozi.moziserver.model.req.ReqUserChallengeList;
import com.mozi.moziserver.model.res.ResConfirmedUserChallengeRecord;
import com.mozi.moziserver.model.res.ResUserChallengeList;
import com.mozi.moziserver.model.res.ResUserChallengeOfReward;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.UserChallengeService;
import com.mozi.moziserver.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserChallengeController {

    private final UserChallengeService userChallengeService;
    private final UserService userService;

    @ApiOperation("유저 챌린지 날짜별 리스트 조회")
    @GetMapping("/v1/period/user-challenges")
    public List<ResUserChallengeList> getUserChallengeList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqUserChallengeList req
    ) {
        if (req.getStartDate().isAfter(req.getEndDate())) {
            throw ResponseError.BadRequest.INVALID_START_DATE_END_DATE.getResponseException();
        }

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

    @ApiOperation("완료한 유저 챌린지 리스트 조회 (유저가 확인 안한 것만)")
    @GetMapping("/v1/user-challenges/end")
    public List<ResUserChallengeOfReward> getEndUserChallengeList(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        return userChallengeService.getEndUserChallengeList(userSeq)
                .stream()
                .map(ResUserChallengeOfReward::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("인증한 제로 활동-활동별 (마이페이지)")
    @GetMapping("/v1/user-challenges/confirmed")
    public List<ResConfirmedUserChallengeRecord> getUserChallengeRecordList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {
        return userChallengeService.getUserChallengeRecordListByUserSeq(userSeq, req)
                .stream()
                .map(ResConfirmedUserChallengeRecord::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("유저 챌린지 생성")
    @PostMapping("/v1/user-challenges")
    public ResponseEntity<Object> createUserChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqUserChallengeCreate req
    ) {

        User user = userService.getUserBySeq(userSeq);
        userChallengeService.createUserChallenge(user, req);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("완료한 유저 챌린지 확인 완료")
    @PutMapping("/v1/user-challenges/{seq}/checked")
    public ResponseEntity<Object> checkedUserChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long userChallengeSeq
    ) {
        userChallengeService.checkUserChallenge(userSeq, userChallengeSeq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("유저 챌린지 그만두기")
    @PutMapping("/v1/user-challenges/{seq}/stop")
    public ResponseEntity<Object> stopUserChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long userChallengeSeq
    ) {
        userChallengeService.stopUserChallenge(userSeq, userChallengeSeq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("유저 챌린지 생성 가능한지 확인")
    @GetMapping("/v1/user-challenges/creatable")
    public ResponseEntity<Object> checkCreatableUserChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqChallengeAndDate req
    ) {

        User user = userService.getUserBySeq(userSeq);
        userChallengeService.checkCreatableUserChallenge(user, req);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
