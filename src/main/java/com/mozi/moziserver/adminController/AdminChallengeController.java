package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminChallengeService;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.adminReq.AdminReqChallengeCreate;
import com.mozi.moziserver.model.adminReq.AdminReqChallengeUpdate;
import com.mozi.moziserver.model.adminReq.AdminReqCurrentTagList;
import com.mozi.moziserver.model.adminReq.AdminReqCurrentThemeList;
import com.mozi.moziserver.model.adminRes.*;
import com.mozi.moziserver.model.entity.Challenge;
import com.mozi.moziserver.model.entity.ChallengeStatistics;
import com.mozi.moziserver.model.entity.ChallengeTheme;
import com.mozi.moziserver.model.entity.Topic;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminChallengeController {
    private final AdminChallengeService adminChallengeService;

    // -------------------- -------------------- Challenge -------------------- -------------------- //
    @ApiOperation("챌린지 하나 조회")
    @GetMapping("/admin/challenges/{seq}")
    public AdminResChallenge getChallenge(
            @PathVariable Long seq
    ) {

        Challenge challenge = adminChallengeService.getChallenge(seq);

        ChallengeTheme challengeTheme = adminChallengeService.getChallengeTheme(challenge.getThemeSeq().intValue());

        List<ChallengeStatistics> challengeStatisticsList = adminChallengeService.getChallengeStatisticsListByPeriod(challenge, 2023, 1);

        return AdminResChallenge.of(challenge, challengeTheme, challengeStatisticsList);
    }

    @ApiOperation("챌린지 리스트 조회")
    @GetMapping("/admin/challenges")
    public List<AdminResChallengeList> getChallengeList(
            @RequestParam(name = "themeSeq", required = false) Long themeSeq,
            @RequestParam(name = "main_tag", required = false) ChallengeTagType tag,
            @RequestParam(name = "keyword", required = false) String keyword, // challenge name
            @RequestParam(name = "topicSeq", required = false) Long topicSeq,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize

    ) {
        List<Challenge> challengeList = adminChallengeService.getChallengeListByThemeAndTagAndName(themeSeq, tag, topicSeq, keyword, pageNumber, pageSize);

        return challengeList
                .stream()
                .map(challenge -> AdminResChallengeList.of(challenge, adminChallengeService.getChallengeTheme(challenge.getThemeSeq().intValue())))
                .collect(Collectors.toList());
    }

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
            @PathVariable Long seq,
            @RequestParam(required = true) String title,
            @RequestParam(value = "content", required = true) List<String> contentList
    ) {
        adminChallengeService.createChallengeExplanation(seq, title, contentList);//        challengeService.createChallenge(req);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 수정")
    @PutMapping("/admin/challenges/{seq}")
    public ResponseEntity<Object> updateChallenge(
            @PathVariable Long seq,
            @Valid AdminReqChallengeUpdate req,
            @RequestParam(required = false) String title,
            @RequestParam(value = "content", required = false) List<String> contentList

    ) {
        if (req.getDescription() != null || req.getName() != null
                || req.getPoint() != null || req.getMainTag() != null
                || req.getRecommendedCnt() != null || req.getThemeSeq() != null
                || title != null || (contentList != null && !contentList.isEmpty())) {

            adminChallengeService.updateChallenge(seq, req, title, contentList);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 삭제")
    @DeleteMapping("/admin/challenges/{seq}")
    public ResponseEntity<Object> deleteChallenge(
            @PathVariable Long seq
    ) {
        adminChallengeService.deleteChallenge(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- Tag -------------------- -------------------- //

    @ApiOperation("태그 리스트 조회")
    @GetMapping("/admin/tags")
    public List<AdminResTag> getTagList(
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        return adminChallengeService.getTagList(keyword)
                .stream()
                .map(tag -> AdminResTag.of(tag))
                .collect(Collectors.toList());
    }

    @ApiOperation("태그 등록")
    @PostMapping("/admin/tags")
    public ResponseEntity<Object> createTag(
            @RequestParam(name = "name") String name
    ) {
        adminChallengeService.createTag(name);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("태그 수정")
    @PutMapping("/admin/tags/{seq}")
    public ResponseEntity<Object> updateTag(
            @PathVariable(name = "seq") Long seq,
            @RequestParam(name = "name") String name
    ) {
        if (name != null) {
            adminChallengeService.updateTag(seq, name);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("태그 삭제")
    @DeleteMapping("/admin/tags/{seq}")
    public ResponseEntity<Object> updateTag(
            @PathVariable(name = "seq") Long seq
    ) {
        adminChallengeService.deleteTag(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- ChallengeTheme -------------------- -------------------- //
    @ApiOperation("테마 리스트 조회")
    @GetMapping("/admin/challege-themes")
    public List<ChallengeTheme> getChallengeThemeList(
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        return adminChallengeService.getChallengeThemeList(keyword);
    }

    @ApiOperation("테마 등록")
    @PostMapping("/admin/challege-themes")
    public ResponseEntity<Object> createChallengeTheme(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "color") String color,
            @RequestParam(value = "inactiveColor") String inactiveColor
    ) {
        adminChallengeService.createChallengeTheme(name, color, inactiveColor);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("테마 수정")
    @PutMapping("/admin/challege-themes/{seq}")
    public ResponseEntity<Object> updateChallengeTheme(
            @PathVariable(name = "seq") Integer seq,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "color", required = false) String color,
            @RequestParam(name = "inactiveColor", required = false) String inactiveColor
    ) {
        if (name != null || color != null || inactiveColor != null) {
            adminChallengeService.updateChallengeTheme(seq, name, color, inactiveColor);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("테마 삭제")
    @DeleteMapping("/admin/challege-themes/{seq}")
    public ResponseEntity<Object> deleteChallengeTheme(
            @PathVariable(name = "seq") Integer seq
    ) {
        adminChallengeService.deleteChallengeTheme(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- CurrentTagList -------------------- -------------------- //
    @ApiOperation("현재 태그 리스트 조회")
    @GetMapping("/admin/current-tag-lists")
    public List<AdminResCurrentTagList> getCurrentTagList(
    ) {
        return adminChallengeService.getAllCurrentTagList()
                .stream()
                .map(currentTagList -> AdminResCurrentTagList.of(currentTagList))
                .collect(Collectors.toList());
    }

    @ApiOperation("현재 태그 리스트 생성")
    @PostMapping("/admin/current-tag-lists")
    public ResponseEntity<Object> createCurrentTagList(
            @RequestParam(name = "turn") Integer turn,
            @RequestParam(name = "tag_seq") Long tagSeq

    ) {
        if (turn != null & tagSeq != null) {
            adminChallengeService.createCurrentTagList(turn, tagSeq);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //모든 태그 리스트를 받아서 수정하기
    @ApiOperation("현재 태그 리스트 수정")
    @PutMapping("/admin/current-tag-lists")
    public ResponseEntity<Object> updateCurrentTagList(
            @RequestBody @Valid List<AdminReqCurrentTagList> req
    ) {
        if (req.size() == adminChallengeService.getAllCurrentTagList().size()) {
            adminChallengeService.updateAllCurrentTagList(req);
        } else {
            throw ResponseError.BadRequest.BAD_REQUEST.getResponseException("need requests for all CurrentTagList");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("현재 태그 리스트 삭제")
    @DeleteMapping("/admin/current-tag-lists/{seq}")
    public ResponseEntity<Object> deleteCurrentTagList(
            @PathVariable(name = "seq") Long seq
    ) {
        adminChallengeService.deleteCurrentTagList(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- CurrentThemeList -------------------- -------------------- //
    @ApiOperation("현재 테마 리스트 조회")
    @GetMapping("/admin/current-theme-lists")
    public List<AdminResCurrentThemeList> getCurrentThemeList(
    ) {
        return adminChallengeService.getAllCurrentThemeList()
                .stream()
                .map(currentThemeList -> AdminResCurrentThemeList.of(currentThemeList))
                .collect(Collectors.toList());
    }

    @ApiOperation("현재 테마 리스트 생성")
    @PostMapping("/admin/current-theme-lists")
    public ResponseEntity<Object> createCurrentThemeList(
            @RequestParam(name = "turn") Integer turn,
            @RequestParam(name = "challenge_theme_seq") Integer challengeThemeSeq

    ) {
        if (turn != null & challengeThemeSeq != null) {
            adminChallengeService.createCurrentThemeList(turn, challengeThemeSeq);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("현재 테마 리스트 수정")
    @PutMapping("/admin/current-theme-lists")
    public ResponseEntity<Object> updateCurrentThemeList(
            @RequestBody @Valid List<AdminReqCurrentThemeList> req
    ) {

        if (req.size() == adminChallengeService.getAllCurrentThemeList().size()) {
            adminChallengeService.updateAllCurrentThemeList(req);
        } else {
            throw ResponseError.BadRequest.BAD_REQUEST.getResponseException("need requests for all CurrentThemeList");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("현재 테마 리스트 삭제")
    @DeleteMapping("/admin/current-theme-lists/{seq}")
    public ResponseEntity<Object> deleteCurrentThemeList(
            @PathVariable(name = "seq") Long seq
    ) {
        adminChallengeService.deleteCurrentThemeList(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- ChallengeTopic -------------------- -------------------- //
    @ApiOperation("전체 챌린지 주제 리스트 조회")
    @GetMapping("/admin/topics")
    public List<Topic> getTopicList() {

        return adminChallengeService.getTopicList();
    }

    @ApiOperation("현재 챌린지 주제 리스트 조회")
    @GetMapping("/admin/current-topic-lists")
    public List<AdminResCurrentTopicList> getCurrentTopicList() {

        List<Topic> topicList = adminChallengeService.getCurrentTopicList();

        return IntStream.range(0, topicList.size())
                .mapToObj(i -> AdminResCurrentTopicList.of(i + 1, topicList.get(i)))
                .collect(Collectors.toList());
    }

    @ApiOperation("챌린지 주제 등록")
    @PostMapping("/admin/topics")
    public ResponseEntity<Object> createTopic(
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "subTitle", required = true) String subTitle,
            @RequestPart(required = true) MultipartFile image,
            @RequestParam(value = "isCurrentTopicList", required = true) boolean isCurrentTopicLists
    ) {

        if (isCurrentTopicLists) {
            adminChallengeService.createTopicAndCurrentTopic(title, subTitle, image);
        } else {
            adminChallengeService.createTopicOnly(title, subTitle, image);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 주제에 챌린지 등록")
    @PostMapping("/admin/challenge-topics")
    public ResponseEntity<Object> createChallengeTopicByTopic(
            @RequestBody(required = true) Long topicSeq,
            @RequestBody(required = true) List<Long> challengeSeqList
    ) {

        List<Long> distinctChallengeSeqList = challengeSeqList.stream().distinct().collect(Collectors.toList());
        if (distinctChallengeSeqList.size() != challengeSeqList.size()) {
            throw ResponseError.BadRequest.CHALLENGE_SEQ_IS_DUPLICATED.getResponseException();
        }

        adminChallengeService.createChallengeTopicByTopic(topicSeq, challengeSeqList);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 주제 수정")
    @PutMapping("/admin/topics/{seq}")
    public ResponseEntity<Object> updateTopic(
            @PathVariable(value = "seq", required = true) Long seq,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "subTitle", required = false) String subTitle,
            @RequestPart(required = false) MultipartFile image
    ) {

        adminChallengeService.updateTopic(seq, title, subTitle, image);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("현재 챌린지 주제 리스트 변경 (순서 변경/ 추가/ 삭제)")
    @PutMapping("/admin/current-topic-list")
    public ResponseEntity<Object> updateCurrentTopicList(
            @RequestBody @Valid List<Long> topicSeqList
    ) {

        List<Long> distinctTopicSeqList = topicSeqList.stream().distinct().collect(Collectors.toList());
        if (topicSeqList.size() != distinctTopicSeqList.size()) {
            throw ResponseError.BadRequest.TOPIC_SEQ_IS_DUPLICATED.getResponseException();
        }

        adminChallengeService.updateAllCurrentTopicList(topicSeqList);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 주제 삭제")
    @DeleteMapping("/admin/topics/{seq}")
    public ResponseEntity<Object> deleteTopic(
            @PathVariable(value = "seq", required = true) Long seq
    ) {

        adminChallengeService.deleteTopic(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 주제에서 챌린지 삭제")
    @DeleteMapping("/admin/challenge-topics")
    public ResponseEntity<Object> deleteChallengeTopic(
            @RequestParam(required = true) Long topicSeq,
            @RequestParam(required = true) Long challengeSeq
    ) {

        adminChallengeService.deleteChallengeTopicByTopicAndChallenge(topicSeq, challengeSeq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}