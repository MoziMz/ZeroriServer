package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminChallengeService;
import com.mozi.moziserver.model.req.ReqAdminChallengeCreate;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.ChallengeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminChallengeController {

    private final AdminChallengeService adminChallengeService;

    @ApiOperation("챌린지 생성")
    @PostMapping("/admin/challenges")
    public ResponseEntity<Object> createChallenge(
            @Valid ReqAdminChallengeCreate req
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
            @RequestParam(value = "content",required = true) List<String> contentList
    ) {
        adminChallengeService.createChallengeExplanation(seq, title, contentList);//        challengeService.createChallenge(req);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
