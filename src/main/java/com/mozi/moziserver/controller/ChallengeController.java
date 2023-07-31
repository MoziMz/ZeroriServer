package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.req.ReqChallengeList;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.model.res.ResChallenge;
import com.mozi.moziserver.model.res.ResChallengeList;
import com.mozi.moziserver.model.res.ResChallengeTagList;
import com.mozi.moziserver.model.res.ResSearchOfChallengeList;
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
import java.util.Collections;
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

        User user = userService.getUserBySeq(userSeq);

        Optional<UserChallenge> optionalUserChallenge = userChallengeService.getActiveUserChallenge(userSeq, challenge);

        Optional<UserChallengeRecord> optionalUserChallengeRecord = optionalUserChallenge.isPresent() ?
                Optional.of(userChallengeService.getUserChallengeRecord(user, challenge)) :
                Optional.empty();

        List<ChallengeStatistics> challengeStatisticsList = challengeStatisticsService.getChallengeStatisticsList(challenge);

        List<ResChallengeTagList> challengeTagList = challengeTagService.getChallengeTagList(seq);

        ChallengeScrap challengeScrap = challengeService.getChallengeScrap(challenge, user);

        Optional<Confirm> optionalConfirm = confirmService.getConfirmByChallenge(challenge);

        return ResChallenge.of(challenge, optionalUserChallenge, optionalUserChallengeRecord, challengeStatisticsList, challengeTagList, challengeScrap, optionalConfirm);
    }

    @ApiOperation("챌린지 모두 조회")
    @GetMapping("/v1/challenges")
    public ResSearchOfChallengeList getChallengeList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqChallengeList req
    ) {

        User user = userService.getUserBySeq(userSeq);

        List<ChallengeScrap> challengeScrapList = challengeScrapService.getChallengeScrapList(user);

        long challengeCnt = challengeService.getChallengeCnt(req);

        List<Challenge> challengeListPaging = challengeService.getChallengeList(req);

        List<ResChallengeList> challengeLists = new ArrayList<ResChallengeList>();

        for (Challenge challenge : challengeListPaging) {
            Boolean flag = false;
            for (ChallengeScrap cs : challengeScrapList) {
                if (cs.getChallenge().equals(challenge)) {
                    challengeLists.add(ResChallengeList.of(challenge, true));
                    flag = true;
                }
            }
            if (!flag) {
                challengeLists.add(ResChallengeList.of(challenge, false));
            }
        }

        if (req.getIsRandom()) {
            ResChallengeList lastResChallenge = challengeLists.get(challengeLists.size() - 1);

            challengeLists.remove(challengeLists.size() - 1);

            Collections.shuffle(challengeLists);

            challengeLists.add(lastResChallenge);
        }

        return ResSearchOfChallengeList.of(challengeCnt, challengeLists);
    }

    @ApiOperation("스크랩한 챌린지 리스트 조회")
    @GetMapping("v1/challenges/scrap")
    public List<ResChallengeList> getScrappedChallengeList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {

        User user = userService.getUserBySeq(userSeq);
        List<Challenge> challengeList = challengeService.getScrappedChallengeList(user, req);

        boolean isScrapped = true;
        return challengeList.stream()
                .map(challenge -> ResChallengeList.of(challenge, true))
                .collect(Collectors.toList());
    }

    @ApiOperation("챌린지 스크랩 생성")
    @PostMapping("/v1/challenges/{seq}/scraps")
    public ResponseEntity<Object> createChallengeScrap(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {

        User user = userService.getUserBySeq(userSeq);
        challengeService.createChallengeScrap(user, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 스크랩 취소")
    @DeleteMapping("/v1/challenges/{seq}/scraps")
    public ResponseEntity<Object> deleteChallengeScrap(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {

        User user = userService.getUserBySeq(userSeq);
        challengeService.deleteChallengeScrap(user, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}