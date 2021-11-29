package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.req.ReqChallengeList;
import com.mozi.moziserver.model.res.ResChallenge;
import com.mozi.moziserver.model.res.ResChallengeList;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.ChallengeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @ApiOperation("챌린지 하나 조회")
    @GetMapping("/v1/challenges/{seq}")
    public List<ResChallenge> getChallenge(
            @PathVariable Long seq
    ) {
        return challengeService.getChallenge(seq)
                .stream()
                .map(ResChallenge::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("챌린지 모두 조회")
    @GetMapping("/v1/challenges")
    public List<ResChallengeList> getChallengeList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqChallengeList req
    ) {

        return challengeService.getChallengeList(userSeq, req)
                .stream()
                .map(ResChallengeList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("챌린지 스크랩")
    @PostMapping("/v1/challenges/{seq}/scrabs")
    public ResponseEntity<Void> createChallengeScrab(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ){
        challengeService.createChallengeScrab(userSeq,seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
