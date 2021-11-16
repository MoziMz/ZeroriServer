package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserIsland;
import com.mozi.moziserver.model.res.ResUserIslandList;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.UserIslandService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserIslandController {

    private final UserIslandService userIslandService;

    @GetMapping("/v1/users/me/user-islands")
    public List<ResUserIslandList> getUserIslandList(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        return userIslandService.getUserIslandList(userSeq)
                .stream()
                .map(ResUserIslandList::of)
                .collect(Collectors.toList());
    }
}
