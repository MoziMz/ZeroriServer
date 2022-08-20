package com.mozi.moziserver.controller;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.req.ReqChallengeList;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.model.res.ResChallenge;
import com.mozi.moziserver.model.res.ResChallengeList;
import com.mozi.moziserver.model.res.ResChallengeTagList;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;
    private final UserChallengeService userChallengeService;
    private final ChallengeStatisticsService challengeStatisticsService;
    private final ChallengeTagService challengeTagService;
    private final ChallengeScrapService challengeScrapService;

    private final ConfirmService confirmService;
    private final UserService userService;

    @ApiOperation("챌린지 하나 조회")
    @GetMapping("/v1/challenges/{seq}")
    public ResChallenge getChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        Challenge challenge = challengeService.getChallenge(seq);
        User user = userService.getUserBySeq(userSeq)
                .orElseThrow(ResponseError.InternalServerError.UNEXPECTED_ERROR::getResponseException);

        Optional<UserChallenge> optionalUserChallenge = userChallengeService.getActiveUserChallenge(userSeq, challenge);

        Optional<UserChallengeRecord> optionalUserChallengeRecord = optionalUserChallenge.isPresent() ?
                Optional.of(userChallengeService.getUserChallengeRecord(userSeq, challenge)) :
                Optional.empty();

        List<ChallengeStatistics> challengeStatisticsList = challengeStatisticsService.getChallengeStatisticsList(challenge);

        List<ResChallengeTagList> challengeTagList = challengeTagService.getChallengeTagList(seq);

        ChallengeScrap challengeScrap = challengeService.getChallengeScrap(challenge, user);

        Optional<Confirm> optionalConfirm=confirmService.getConfirmByChallenge(challenge);

        return ResChallenge.of(challenge, optionalUserChallenge, optionalUserChallengeRecord, challengeStatisticsList, challengeTagList, challengeScrap,optionalConfirm);
    }

    @ApiOperation("챌린지 모두 조회")
    @GetMapping("/v1/challenges")
    public List<ResChallengeList> getChallengeList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqChallengeList req
    ) {
        List<ChallengeScrap> challengeScrapList=challengeScrapService.getChallengeScrapList(userSeq);

        List<Challenge> challengeList=challengeService.getChallengeList(userSeq, req);

        List<ResChallengeList> challengeLists=new ArrayList<ResChallengeList>();

        for(Challenge challenge: challengeList){
            Boolean flag=false;
            for(ChallengeScrap cs: challengeScrapList){
                if(cs.getChallenge().equals(challenge)){
                    challengeLists.add(ResChallengeList.of(challenge,true));
                    flag=true;
                }
            }
            if(!flag) challengeLists.add(ResChallengeList.of(challenge,false));
        }

        return challengeLists;
    }

    @ApiOperation("스크랩한 챌린지 리스트 조회")
    @GetMapping("v1/challenges/scrap")
    public List<ResChallengeList> getScrappedChallengeList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {
        List<Challenge> challengeList = challengeService.getScrappedChallengeList(userSeq, req);

        boolean isScrapped = true;
        return challengeList.stream()
                .map(challenge -> ResChallengeList.of(challenge, true))
                .collect(Collectors.toList());
    }

    @ApiOperation("챌린지 스크랩")
    @PostMapping("/v1/challenges/{seq}/scraps")
    public ResponseEntity<Void> createChallengeScrap(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        challengeService.createChallengeScrap(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 스크랩")
    @DeleteMapping("/v1/challenges/{seq}/scraps")
    public ResponseEntity<Void> deleteChallengeScrap(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        challengeService.deleteChallengeScrap(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

