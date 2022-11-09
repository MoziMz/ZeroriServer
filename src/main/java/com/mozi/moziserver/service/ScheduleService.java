package com.mozi.moziserver.service;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

import javax.transaction.Transactional;
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

    private final UserNoticeRepository userNoticeRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
//    For test
//    @Scheduled(initialDelay = 1000L, fixedDelay = 100000000000000L)
    public void updateUserChallenge() {
        final LocalDate today = LocalDate.now();

//        TODO 트렌젝션 지연시간 줄이기 위해 변화시킬 방향
//        try{
//            전체 조회 ( 조건에 맞는 대상만 가져옴 ) -- 트렌젝션 X
//
//            하나 조회 ( 조건에 맞는지 한번더 확인 ) -- 트렌젝션 O
//            하나 업데이트 -- 트렌젝션 O
//        } catch () {}

        // TODO return 되는 값은 로그로 남긴다.
        userChallengeRepository.updateState(today, UserChallengeStateType.PLAN, UserChallengeStateType.DOING);

        updateUserChallengeDoingToEnd(today);
    }

    /**
     * user challenge state from doing to end
     *
     * @param date start date
     */
    public void updateUserChallengeDoingToEnd(final LocalDate date) {
        // TODO 트렌젝션 걸기

        List<UserChallenge> endedUserChallengeList = userChallengeRepository.findAllByStateAndStartDate(UserChallengeStateType.DOING, date.minusDays(7));

        for (UserChallenge userChallenge : endedUserChallengeList) {
                UserChallenge curUserChallenge = userChallengeRepository.findBySeq(userChallenge.getSeq())
                        .orElse(null);

                if (curUserChallenge == null) {
                    // TODO
                }

                if (curUserChallenge.getTotalConfirmCnt() >= curUserChallenge.getChallenge().getRecommendedCnt()) {
                    userRewardRepository.incrementPoint(curUserChallenge.getUser().getSeq(), Constant.challengeExtraPoints);
                }

                userChallengeRepository.updateUserChallengeState(curUserChallenge.getSeq(), UserChallengeStateType.DOING, UserChallengeStateType.END);
        }
    }

    @Scheduled(cron = "0 0 21 ? * SUN")
//    @Scheduled(initialDelay = 1000L, fixedDelay = 100000000000000L) //    For test
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
//            User curUser = userRepository.findById(user.getSeq()).orElse(null);
//            if (curUser == null) {
//                // TODO 탈퇴한 유저를 재조회하면 좋음
//                continue;
//            }
            PostboxMessageAnimal lastPostboxMessageAnimal = postboxMessageAnimalRepository.findLastOneByUser(user);

            withTransaction(() -> {

                lastPostboxMessageAnimal.setLevel(lastPostboxMessageAnimal.getLevel() + 1);
                lastPostboxMessageAnimal.setCheckedState(false);
                postboxMessageAnimalRepository.save(lastPostboxMessageAnimal);

                if (lastPostboxMessageAnimal.getLevel() == 3) {
                    if (lastPostboxMessageAnimal.getAnimal().getIslandLevel() < Constant.islandMaxLevel) {
                        Animal nextAnimal = animalRepository.findByIslandTypeAndIslandLevel(lastPostboxMessageAnimal.getAnimal().getIslandType(), lastPostboxMessageAnimal.getAnimal().getIslandLevel() + 1);
                        postboxMessageAnimalService.createPostboxMessageAnimal(user, nextAnimal);
                    }
                    userIslandRepository.updateUserIslandRewardLevel(user.getSeq(),lastPostboxMessageAnimal.getAnimal().getIslandType());
                }

                //동물의 편지 알림
                UserNotice userNotice = UserNotice.builder()
                        .user(user)
                        .checkedState(false)
                        .type(UserNoticeType.PostboxMessageAnimal.ordinal())
                        .build();

                userNoticeRepository.save(userNotice);

            });

            fcmService.sendMessageToUser(lastPostboxMessageAnimal.getUser(), FcmMessageType.NEW_POST_BOX_MESSAGE);
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
