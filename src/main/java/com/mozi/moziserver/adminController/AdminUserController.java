package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminUserService;
import com.mozi.moziserver.common.UserState;
import com.mozi.moziserver.model.adminRes.AdminResUserList;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @ApiOperation("유저 리스트 조회")
    @GetMapping("/admin/users")
    public List<AdminResUserList> getUserList(
            @RequestParam(name = "keyword", required = false) String keyword, // keyword is userNickName or userEmail
            @RequestParam(name = "type", required = false) UserAuthType type,
            @RequestParam(name = "state", required = false) UserState state,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize
    ) {
        List<UserAuth> userAuthList = adminUserService.getUserAuthList(keyword, type, state, pageNumber, pageSize);

        return userAuthList
                .stream()
                .map((userAuth -> AdminResUserList.of(userAuth, userAuth.getUser(), userAuth.getUser().getUserReward())))
                .collect(Collectors.toList());
    }
}
