package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.DetailIsland;
import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserIsland;
import com.mozi.moziserver.model.res.ResUserIslandList;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.IslandService;
import com.mozi.moziserver.service.UserRewardService;
import com.mozi.moziserver.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserIslandController {

    private final IslandService islandService;
    private final UserRewardService userRewardService;
    private final UserService userService;

    // TODO ERASE (NOT USED V2)
    @Operation(summary = "섬리스트 조회")
    @GetMapping("/v1/users/me/user-islands")
    public List<ResUserIslandList> getUserIslandListV1(
            @Parameter(hidden = true) @SessionUser Long userSeq
    ) {

        User user = userService.getUserBySeq(userSeq);
        List<UserIsland> userIslandList = islandService.getUserIslandListOrderByIslandSeq(user);
        List<Island> islandList = islandService.getIslandListOrderBySeq();
        int currentUserPoint = userRewardService.getUserPoint(user);
        UserIsland lastUserIsland = userIslandList.get(userIslandList.size() - 1);

        List<ResUserIslandList> resUserIslandLists = new LinkedList<ResUserIslandList>();
        for (int i = 0; i < islandList.size(); i++) {
            Island island = islandList.get(i);
            UserIsland userIsland = i < userIslandList.size() ? userIslandList.get(i) : null;
            resUserIslandLists.add(ResUserIslandList.of(island, userIsland, lastUserIsland, currentUserPoint));
        }

        return resUserIslandLists;
    }

    @Operation(summary = "섬 오픈하기")
    @PostMapping("/v1/users/me/user-islands/open")
    public ResponseEntity<Object> openUserIslandV1(
            @Parameter(hidden = true) @SessionUser Long userSeq
    ) {

        User user = userService.getUserBySeq(userSeq);
        islandService.openUserIsland(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- v2 -------------------- //
    @Operation(summary = "섬 리스트 조회")
    @GetMapping("/v2/users/me/user-islands")
    public List<ResUserIslandList> getUserIslandList(
            @Parameter(hidden = true) @SessionUser Long userSeq
    ) {

        User user = userService.getUserBySeq(userSeq);
        List<UserIsland> userIslandList = islandService.getUserIslandListOrderByIslandSeq(user);
        List<Island> islandList = islandService.getIslandListOrderBySeq();
        int currentUserPoint = userRewardService.getUserPoint(user);
        Long lastUserIslandSeq = userIslandList.get(userIslandList.size() - 1).getDetailIsland().getIsland().getSeq();

        List<ResUserIslandList> resUserIslandLists = new LinkedList<ResUserIslandList>();
        for (int i = 0; i < islandList.size(); i++) {
            Island island = islandList.get(i);
            DetailIsland detailIsland = i < userIslandList.size() ? userIslandList.get(i).getDetailIsland() : null;
            resUserIslandLists.add(ResUserIslandList.of(island, detailIsland, lastUserIslandSeq, currentUserPoint));
        }

        return resUserIslandLists;
    }
}
