package com.mozi.moziserver.controller;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.req.ReqChallengeAndDate;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.model.req.ReqUserChallengeCreate;
import com.mozi.moziserver.model.req.ReqUserChallengeList;
import com.mozi.moziserver.model.res.ResConfirmedUserChallenge;
import com.mozi.moziserver.model.res.ResUserChallengeList;
import com.mozi.moziserver.model.res.ResUserChallengeOfReward;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserChallengeController {
    private final UserChallengeService userChallengeService;

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

    @ApiOperation("완료한 유저 챌린지 확인 완료")
    @PutMapping("/v1/user-challenges/{seq}/checked")
    public ResponseEntity<Void> checkedUserChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long userChallengeSeq
    ) {
        userChallengeService.checkUserChallenge(userSeq, userChallengeSeq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("인증한 제로 활동-활동별 (마이페이지)")
    @GetMapping("/v1/user-challenges/confirmed")
    public List<ResConfirmedUserChallenge> getUserChallengeRecordList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {
        return userChallengeService.getUserChallengeRecordListByUserSeq(userSeq, req)
                .stream()
                .map(ResConfirmedUserChallenge::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("유저 챌린지 생성 가능한지 확인")
    @GetMapping("/v1/user-challenges/creatable")
    public ResponseEntity<Void> getCreatableUserChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqChallengeAndDate req
    ) {
        userChallengeService.isCreatableUserChallenge(userSeq,req);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
