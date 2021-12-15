package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.res.ResCurrentThemeList;
import com.mozi.moziserver.service.CurrentThemeListService;
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
public class CurrentThemeListController {
    private final CurrentThemeListService currentThemeListService;

    @ApiOperation("테마 리스트 조회")
    @GetMapping("/v1/themes")
    public List<ResCurrentThemeList> getCurrentThemeList() {

        return currentThemeListService.getCurrentThemeList()
                .stream()
                .map(ResCurrentThemeList::of)
                .collect(Collectors.toList());
    }
}
