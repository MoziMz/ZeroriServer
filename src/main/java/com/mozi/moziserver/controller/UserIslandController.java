package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.model.entity.UserIsland;
import com.mozi.moziserver.model.res.ResUserIslandList;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.IslandService;
import com.mozi.moziserver.service.UserRewardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserIslandController {

    private final IslandService islandService;
    private final UserRewardService userRewardService;

    @ApiOperation("섬리스트 조회")
    @GetMapping("/v1/users/me/user-islands")
    public List<ResUserIslandList> getUserIslandList(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        List<UserIsland> userIslandList = islandService.getUserIslandList(userSeq);
        List<Island> islandList = islandService.getIslandList();

        int currentUserPoint = userRewardService.getUserPoint(userSeq);
        int finalUserIsland = userIslandList.size() + 1;
        List<ResUserIslandList> resUserIslandLists = new LinkedList<ResUserIslandList>();
        for (int i = 0; i < islandList.size(); i++) {
            UserIsland userIsland = null;
            if (i < userIslandList.size())
                userIsland = userIslandList.get(i);

            resUserIslandLists.add(ResUserIslandList.of(islandList.get(i), userIsland, finalUserIsland, currentUserPoint));
        }

        return resUserIslandLists;
    }

    @ApiOperation("섬 오픈하기")
    @PostMapping("/v1/users/me/user-islands/open")
    public ResponseEntity<Void> openUserIsland(
        @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        islandService.openUserIsland(userSeq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
