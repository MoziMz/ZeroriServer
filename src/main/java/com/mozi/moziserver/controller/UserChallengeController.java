package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.req.ReqChallengeList;
import com.mozi.moziserver.model.res.ResChallenge;
import com.mozi.moziserver.model.res.ResChallengeList;
import com.mozi.moziserver.model.res.ResUserChallenge;
import com.mozi.moziserver.repository.ChallengeRepository;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.ChallengeService;
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

//    @ApiOperation("유저 챌린지 하나 조회")
//    @GetMapping({"/v1/challenges/{seq}/user-challenges"})
//    public ResUserChallenge getUserChallenge(
//            @ApiParam(hidden = true) @SessionUser Long userSeq,
//            @PathVariable("seq") Long seq
//    ){
//        Challenge challenge = challengeRepository.findById(seq).get();
//        return ResUserChallenge.of(userChallengeService.getUserChallenge(userSeq, challenge));
//    }

}
