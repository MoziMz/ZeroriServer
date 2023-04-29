package com.mozi.moziserver.service;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.FcmMessageType;
import com.mozi.moziserver.model.mappedenum.PointReasonType;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import com.mozi.moziserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Component
public class ScheduleService {


    private final PostboxMessageAnimalService postboxMessageAnimalService;
    private final UserRewardService userRewardService;
    private final FcmService fcmService;
    private final IslandService islandService;
    private final AnimalService animalService;

    private final UserChallengeRepository userChallengeRepository;
    private final UserRewardRepository userRewardRepository;
    private final UserRepository userRepository;

    private final PlatformTransactionManager transactionManager;

    //    For test
//    @Scheduled(initialDelay = 1000L, fixedDelay = 100000000000000L)
    @Scheduled(cron = "0 0 0 * * *")
    public void updateUserChallenge() {
        final LocalDate today = LocalDate.now();

        // TODO return 되는 값은 로그로 남긴다.
        userChallengeRepository.updateState(today, UserChallengeStateType.PLAN, UserChallengeStateType.DOING);

        updateUserChallengeDoingToEnd(today);
    }

    public void updateUserChallengeDoingToEnd(final LocalDate date) {
        List<UserChallenge> endedUserChallengeList = userChallengeRepository.findAllByStateAndStartDate(UserChallengeStateType.DOING, date.minusDays(7));

        for (UserChallenge userChallenge : endedUserChallengeList) {
            withTransaction(() -> {
                if (userChallenge.getTotalConfirmCnt() >= userChallenge.getChallenge().getRecommendedCnt()) {
                    userRewardService.incrementPoint(userChallenge.getUser(), PointReasonType.CHALLENGE_EXTRA_POINT, Constant.challengeExtraPoints);
                }

                userChallengeRepository.updateUserChallengeState(userChallenge.getSeq(), UserChallengeStateType.DOING, UserChallengeStateType.END);
            });

            fcmService.sendMessageToUser(userChallenge.getUser(), FcmMessageType.END_USER_CHALLENGE_MESSAGE);
        }
    }

//    For test
//    @Scheduled(initialDelay = 1000L, fixedDelay = 100000000000000L)
    @Scheduled(cron = "0 0 21 ? * SUN")
    public void updatePostboxAnimalByUserPointRecord() {

        final LocalDate today = LocalDate.now();

        LocalDate beforeDay = today.minusDays(7);
        LocalDateTime startDateTime = LocalDateTime.of(beforeDay.getYear(), beforeDay.getMonthValue(), beforeDay.getDayOfMonth(), 21, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(today.getYear(), today.getMonthValue(), today.getDayOfMonth(), 21, 0, 0);
        List<User> activeUserList = userRepository.findAllActiveUserByUserReward(startDateTime, endDateTime);

        for (User user : activeUserList) {

            // step1. 유저의 현재 섬이 마지막 단계이면 패스한다. (animal_turn=섬의 동물수, item_turn = 섬의 마지막 동물의 아이템수)
            UserIsland lastUserIsland = islandService.getLastUserIsland(user);
            DetailIsland lastDetailIsland = lastUserIsland.getDetailIsland();
            if (islandService.isLastDetailIslandOfIsland(lastDetailIsland)) {
                continue;
            }

            // step2. 유저가 다음 아이템의 획득 포인트를 만족하지 못하면 패스한다.
            int nextItemAcquisitionRequiredPoint = animalService.getNextItemAcquisitionRequiredPoint(
                    lastDetailIsland.getIsland().getSeq(),
                    lastDetailIsland.getAnimalTurn(),
                    lastDetailIsland.getItemTurn());
            int userWeekPoint = userRewardService.getPointOfUserPointRecordByPeriod(user, startDateTime, endDateTime);
            if (userWeekPoint < nextItemAcquisitionRequiredPoint) {
                continue;
            }

            // step3. 현재 동물의 편지에 아이템을 추가하고 현재 섬을 업그레이드한다.
            AtomicInteger newMessageCnt = new AtomicInteger(0);
            withTransaction(() -> {
                newMessageCnt.set(postboxMessageAnimalService.incrementPostboxMessageAnimalItem(user));
                islandService.upgradeUserIsland(lastUserIsland, lastDetailIsland);
            });

            // step5. 모든 과정이 문제 없이 완료되었다면 FCM 푸시 알림을 보낸다.
            if (newMessageCnt.get() > 0) {
                fcmService.sendMessageToUser(user, FcmMessageType.POSTBOX_MESSAGE_ANIMAL_NEW_ARRIVED);
            }
            fcmService.sendMessageToUser(user, FcmMessageType.NEW_POST_BOX_MESSAGE);
            fcmService.sendMessageToUser(user, FcmMessageType.POSTBOX_MESSAGE_ANIMAL_RECEIVED_ITEM);
        }
    }

    private void withTransaction(Runnable runnable) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();

        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            runnable.run();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
    }
}