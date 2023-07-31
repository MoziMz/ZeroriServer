package com.mozi.moziserver.controller;


import com.mozi.moziserver.model.entity.ChallengeTheme;
import com.mozi.moziserver.model.entity.Topic;
import com.mozi.moziserver.model.res.ResCurrentTagList;
import com.mozi.moziserver.service.ChallengeService;
import com.mozi.moziserver.service.CurrentTagListService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConstantController {

    private final CurrentTagListService currentTagListService;
    private final ChallengeService challengeService;

    @ApiOperation("태그 리스트 조회")
    @GetMapping("/v1/tags")
    public List<ResCurrentTagList> getCurrentTagList() {

        return currentTagListService.getCurrentTagList()
                .stream()
                .map(ResCurrentTagList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("챌린지 테마 리스트 조회")
    @GetMapping("/v1/challenge-themes")
    public List<ChallengeTheme> getChallengeThemeList() {

        return challengeService.getChallengeThemeList();
    }

    @ApiOperation("챌린지 주제 리스트 조회")
    @GetMapping("v1/challenge-topics")
    public List<Topic> getChallengeTopicList() {

        return challengeService.getChallengeTopicList();
    }
}