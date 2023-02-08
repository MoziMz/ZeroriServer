package com.mozi.moziserver.adminService;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserIsland;
import com.mozi.moziserver.model.entity.UserPointRecord;
import com.mozi.moziserver.model.entity.UserReward;
import com.mozi.moziserver.model.mappedenum.PointReasonType;
import com.mozi.moziserver.repository.UserIslandRepository;
import com.mozi.moziserver.repository.UserPointRecordRepository;
import com.mozi.moziserver.repository.UserRewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminUserRewardService {
    // 유저 포인트, 유저 섬 관련된 로직 관리

    private final AdminUserService adminUserService;

    private final UserRewardRepository userRewardRepository;
    private final UserIslandRepository userIslandRepository;
    private final UserPointRecordRepository userPointRecordRepository;

    public List<UserReward> getUserRewardList(String keyword, Integer pageNumber, Integer pageSize) {

        Long numberOfKeyword = null;
        if (StringUtils.hasLength(keyword)) {
            try {
                numberOfKeyword = Long.valueOf(keyword);
            } catch (NumberFormatException e) {
            }
        }

        return userRewardRepository.findAllByKeyword(keyword, numberOfKeyword, pageNumber, pageSize);
    }

    public List<UserIsland> getLastUserIslandList(List<User> userList) {

        List<UserIsland> userIslandList = userIslandRepository.findAllByUserIn(userList);

        Map<Long, UserIsland> userIslandMap = new HashMap<>(); // key is User's seq
        for (UserIsland userIsland : userIslandList) {
            Long userSeq = userIsland.getUser().getSeq();
            if (userIslandMap.containsKey(userSeq)) {
                UserIsland befUserIsland = userIslandMap.get(userSeq);
                if (befUserIsland.getType() < userIsland.getType()) {
                    userIslandMap.put(userSeq, userIsland);
                }
            } else {
                userIslandMap.put(userSeq, userIsland);
            }
        }

        return userList.stream()
                .map(user -> userIslandMap.get(user.getSeq()))
                .collect(Collectors.toList());
    }

    public List<UserPointRecord> getUserPointRecordByUserAndType(Long userSeq, List<PointReasonType> reasonTypeList, Integer pageNumber, Integer pageSize) {

        User user = adminUserService.getById(userSeq);

        if (reasonTypeList != null && reasonTypeList.size() > 0) {
            return userPointRecordRepository.findAllByUserAndReasonInOrderByCreatedAtDesc(user, reasonTypeList, PageRequest.of(pageNumber, pageSize));
        } else {
            return userPointRecordRepository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(pageNumber, pageSize));
        }
    }
}
