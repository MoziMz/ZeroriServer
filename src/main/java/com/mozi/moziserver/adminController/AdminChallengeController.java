package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminChallengeService;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.adminReq.AdminReqChallengeCreate;
import com.mozi.moziserver.model.adminReq.AdminReqChallengeUpdate;
import com.mozi.moziserver.model.adminRes.*;
import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeStatistics;
import com.mozi.moziserver.model.entity.ChallengeTheme;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import com.mozi.moziserver.security.SessionUser;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminChallengeController {

    private final AdminChallengeService adminChallengeService;

    @ApiOperation("챌린지 생성")
    @PostMapping("/admin/challenges")
    public ResponseEntity<Object> createChallenge(
            @Valid AdminReqChallengeCreate req
    ) {
        adminChallengeService.createChallenge(req);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 설명 추가")
    @PostMapping("/admin/challenges/{seq}/explanations")
    public ResponseEntity<Object> createChallengeTest(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq,
            @RequestParam(required = true) String title,
            @RequestParam(value = "content", required = true) List<String> contentList
    ) {
        adminChallengeService.createChallengeExplanation(seq, title, contentList);//        challengeService.createChallenge(req);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 리스트 조회")
    @GetMapping("/admin/challenges")
    public List<AdminResChallengeList> getChallengeList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestParam(name = "themeSeq", required = false) Long themeSeq,
            @RequestParam(name = "main_tag", required = false) ChallengeTagType tag,
            @RequestParam(name = "keyword", required = false) String keyword, // challenge name
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize

    ) {
        List<Challenge> challengeList = adminChallengeService.getChallengeListByThemeAndTagAndName(themeSeq, tag, keyword, pageNumber, pageSize);

        return challengeList
                .stream()
                .map(challenge -> AdminResChallengeList.of(challenge, adminChallengeService.getChallengeTheme(challenge.getThemeSeq())))
                .collect(Collectors.toList());
    }

    @ApiOperation("챌린지 하나 조회")
    @GetMapping("/admin/challenges/{seq}")
    public AdminResChallenge getChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        Challenge challenge = adminChallengeService.getChallenge(seq);

        ChallengeTheme challengeTheme = adminChallengeService.getChallengeTheme(challenge.getThemeSeq());

        List<ChallengeStatistics> challengeStatisticsList = adminChallengeService.getChallengeStatisticsListByPeriod(challenge, 2023, 1);

        return AdminResChallenge.of(challenge, challengeTheme, challengeStatisticsList);
    }

    @ApiOperation("챌린지 수정")
    @PutMapping("/admin/challenges/{seq}")
    public ResponseEntity<Object> updateChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq,
            @Valid AdminReqChallengeUpdate req,
            @RequestParam(required = false) String title,
            @RequestParam(value = "content", required = false) List<String> contentList

    ) {
        if (req.getDescription() != null || req.getName() != null
                || req.getPoint() != null || req.getMainTag() != null
                || req.getRecommendedCnt() != null || req.getThemeSeq() != null
                || title != null || !contentList.isEmpty()) {

            adminChallengeService.updateChallenge(seq, req, title, contentList);

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
