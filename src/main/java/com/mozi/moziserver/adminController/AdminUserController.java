package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminUserRewardService;
import com.mozi.moziserver.adminService.AdminUserService;
import com.mozi.moziserver.common.UserState;
import com.mozi.moziserver.model.adminRes.AdminResUserList;
import com.mozi.moziserver.model.adminRes.AdminResUserPointRecordList;
import com.mozi.moziserver.model.adminRes.AdminResUserRewardList;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.entity.UserIsland;
import com.mozi.moziserver.model.entity.UserReward;
import com.mozi.moziserver.model.mappedenum.PointReasonType;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Max;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final AdminUserRewardService adminUserRewardService;

    // -------------------- -------------------- UserPointRecord -------------------- -------------------- //
    @Operation(summary = "유저별 포인트 이력 조회")
    @GetMapping("/admin/users/{seq}/user-point-records")
    public List<AdminResUserPointRecordList> getUserPointRecordList(
            @PathVariable("seq") Long userSeq,
            @RequestParam(name = "reasonType", required = false) List<PointReasonType> reasonTypeList,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize
    ) {

        return adminUserRewardService.getUserPointRecordByUserAndType(userSeq, reasonTypeList, pageNumber, pageSize)
                .stream().map(AdminResUserPointRecordList::of)
                .collect(Collectors.toList());
    }

    // -------------------- -------------------- ETC -------------------- -------------------- //
    @Operation(summary = "유저 리스트 조회")
    @GetMapping("/admin/users")
    public List<AdminResUserList> getUserList(
            @RequestParam(name = "keyword", required = false) String keyword, // keyword is userNickName or userEmail
            @RequestParam(name = "type", required = false) UserAuthType type,
            @RequestParam(name = "state", required = false) UserState state,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize
    ) {

        List<UserAuth> userAuthList = adminUserService.getUserAuthList(keyword, type, state, pageNumber, pageSize)
                .stream()
                .sorted(Comparator.comparing(userAuth -> userAuth.getUser().getSeq()))
                .collect(Collectors.toList());

        List<User> userList = userAuthList.stream().map(userAuth -> userAuth.getUser()).collect(Collectors.toList());

        List<UserReward> userRewardList = adminUserRewardService.getAllByUser(userList)
                .stream()
                .sorted(Comparator.comparing(userReward -> userReward.getUser().getSeq()))
                .collect(Collectors.toList());

        return IntStream.range(0, userRewardList.size())
                .mapToObj(i -> AdminResUserList.of(userAuthList.get(i), userAuthList.get(i).getUser(), userRewardList.get(i)))
                .collect(Collectors.toList());
    }

    @Operation(summary = "유저 보상(포인트/섬) 조회")
    @GetMapping("/admin/user-rewards")
    public List<AdminResUserRewardList> getUserRewardList(
            @RequestParam(name = "keyword", required = false) String keyword, // keyword is userSeq or userNickname
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize
    ) {

        List<UserReward> userRewardList = adminUserRewardService.getUserRewardList(keyword, pageNumber, pageSize);

        List<User> userList = userRewardList.stream().map(UserReward::getUser).collect(Collectors.toList());

        List<UserIsland> userIslandList = adminUserRewardService.getLastUserIslandList(userList); // 리스트 순서가 userSeq를 기준으로 userRewardList와 순서가 동일해야함

        return IntStream.range(0, userRewardList.size())
                .mapToObj(i -> AdminResUserRewardList.of(userRewardList.get(i).getUser(), userRewardList.get(i), userIslandList.get(i)))
                .collect(Collectors.toList());
    }
}