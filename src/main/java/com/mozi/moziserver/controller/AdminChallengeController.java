package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.req.ReqAdminChallenge;
import com.mozi.moziserver.service.ChallengeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminChallengeController {
    private final ChallengeService challengeService;

    @ApiOperation("챌린지 생성")
    @PostMapping("/v1/admin/challenges")
    public ResponseEntity<Void> createChallenge(
        @Valid ReqAdminChallenge req
    ){
        challengeService.createChallenge(req);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
