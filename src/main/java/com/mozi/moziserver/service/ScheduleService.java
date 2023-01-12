package com.mozi.moziserver.service;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.FcmMessageType;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import com.mozi.moziserver.model.mappedenum.UserNoticeType;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ScheduleService {

    private final UserChallengeRepository userChallengeRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserChallengeRecordRepository userChallengeRecordRepository;
    private final UserRewardRepository userRewardRepository;
    private final UserRepository userRepository;
    private final PostboxMessageAnimalRepository postboxMessageAnimalRepository;
    private final AnimalRepository animalRepository;
    private final PostboxMessageAnimalService postboxMessageAnimalService;
    private final UserRewardService userRewardService;
    private final UserIslandRepository userIslandRepository;
    private final FcmService fcmService;
    private final UserNoticeService userNoticeService;


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
                    userRewardRepository.incrementPoint(userChallenge.getUser().getSeq(), Constant.challengeExtraPoints);
                }

                userChallengeRepository.updateUserChallengeState(userChallenge.getSeq(), UserChallengeStateType.DOING, UserChallengeStateType.END);
            });

            fcmService.sendMessageToUser(userChallenge.getUser(), FcmMessageType.END_USER_CHALLENGE_MESSAGE);
        }
    }

    @Scheduled(cron = "0 0 21 ? * SUN")
    public void updatePostboxAnimalByUserPointRecord() {

        final LocalDate today = LocalDate.now();

        LocalDate beforeDay = today.minusDays(7);
        LocalDateTime startDateTime = LocalDateTime.of(beforeDay.getDayOfYear(), beforeDay.getMonthValue(), beforeDay.getDayOfMonth(), 21, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(today.getYear(), today.getMonthValue(), today.getDayOfMonth(), 21, 0, 0);
        List<User> activeUserList = userRepository.findAllActiveUserByUserReward(startDateTime, endDateTime);

        // 1. 유저가 최근 일주일동안 30포인트를 획득했는지 확인한다.
        List<User> accomplishedUser = activeUserList.stream()
                .filter(user -> userRewardService.getPointOfUserPointRecord(user, startDateTime, endDateTime) >= 30)
                .collect(Collectors.toList());

        // 2. 조건에 만족하는 유저면 포스트박스와 상황에 맞게 유저 아일랜드를 업그레이드 해준다.
        for (User user : accomplishedUser) {
            PostboxMessageAnimal lastPostboxMessageAnimal = postboxMessageAnimalRepository.findLastOneByUser(user);

            // step. 유저가 섬의 '최종 상태'(마지막 동물이 위시리스트를 모두 받은 상태)인지 확인
            // 조건문을 만족한다면 continue <- 해당 유저는 현재 단계에서 변화할 부분이 없다.
            // TODO 추후 메서드로 분리
            boolean isLastPostboxMessageAnimalStateInIsland = lastPostboxMessageAnimal.getAnimal().getIslandLevel().equals(Constant.islandMaxLevel)
                    && lastPostboxMessageAnimal.getLevel().equals(Constant.postboxAnimalMaxLevel);
            if (isLastPostboxMessageAnimalStateInIsland) continue;

            withTransaction(() -> {
                lastPostboxMessageAnimal.setLevel(lastPostboxMessageAnimal.getLevel() + 1);
                lastPostboxMessageAnimal.setCheckedState(false);
                postboxMessageAnimalRepository.save(lastPostboxMessageAnimal);

                if (lastPostboxMessageAnimal.getLevel() == 3) {
                    if (lastPostboxMessageAnimal.getAnimal().getIslandLevel() < Constant.islandMaxLevel) {
                        Animal nextAnimal = animalRepository.findByIslandTypeAndIslandLevel(lastPostboxMessageAnimal.getAnimal().getIslandType(), lastPostboxMessageAnimal.getAnimal().getIslandLevel() + 1);
                        postboxMessageAnimalService.createPostboxMessageAnimal(user, nextAnimal);
                        userIslandRepository.updateUserIslandRewardLevel(user.getSeq(), lastPostboxMessageAnimal.getAnimal().getIslandType());
                    }
                }
                
                userNoticeService.upsertUserNotice(user, UserNoticeType.POSTBOX_MESSAGE_ANIMAL_RECEIVED_ITEM, lastPostboxMessageAnimal.getSeq());
            });

            if (lastPostboxMessageAnimal.getLevel() == 3 && lastPostboxMessageAnimal.getAnimal().getIslandLevel() < Constant.islandMaxLevel) {
                fcmService.sendMessageToUser(lastPostboxMessageAnimal.getUser(), FcmMessageType.POSTBOX_MESSAGE_ANIMAL_NEW_ARRIVED);
            }


            fcmService.sendMessageToUser(lastPostboxMessageAnimal.getUser(), FcmMessageType.NEW_POST_BOX_MESSAGE);
            fcmService.sendMessageToUser(lastPostboxMessageAnimal.getUser(), FcmMessageType.POSTBOX_MESSAGE_ANIMAL_RECEIVED_ITEM);
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
