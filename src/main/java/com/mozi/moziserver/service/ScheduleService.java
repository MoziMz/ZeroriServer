package com.mozi.moziserver.service;

import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import com.mozi.moziserver.repository.UserChallengeRecordRepository;
import com.mozi.moziserver.repository.UserChallengeRepository;
import com.mozi.moziserver.repository.UserRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ScheduleService {
    private final UserChallengeRepository userChallengeRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserChallengeRecordRepository userChallengeRecordRepository;
    private final UserRewardRepository userRewardRepository;

    // TODO 전역변수로 분리하기
    final int extraPoints = 5;

    //      @Transactional
    @Scheduled(cron = "0 0 0 * * *")
//    For test
//    @Scheduled(initialDelay = 1000L, fixedDelay = 100000000000000L)
    public void updateUserChallenge() {
        final LocalDate today = LocalDate.now();

        // 아래 코드에서 중간에 에러가 나면 로그로 남기고 실행이 안된 시점부터 재실행 한다.

//        withTransaction(() -> {
//            // PlanResult 에서 PLAN 을 FAIL 로 바꾸기
//            LocalDate yesterday = today.minusDays(1);
//            List<UserChallenge> userChallengeList =
//                    userChallengeRepository.findAllByPlanResult(yesterday, UserChallengeResultType.PLAN);
//
//            userChallengeList.stream()
//                    .forEach(item -> item.getPlanDateList()
//                            .get(Period.between(item.getStartDate(), yesterday).getDays())
//                            .setResult(UserChallengeResultType.FAIL));
//
//            userChallengeRepository.saveAll(userChallengeList);
//        });


//        TODO 트렌젝션 지연시간 줄이기 위해 변화시킬 방향
//        try{
//            전체 조회 ( 조건에 맞는 대상만 가져옴 ) -- 트렌젝션 X
//
//            하나 조회 ( 조건에 맞는지 한번더 확인 ) -- 트렌젝션 O
//            하나 업데이트 -- 트렌젝션 O
//        } catch () {}

        // TODO
        // return 되는 값은 로그로 남긴다.

        withTransaction(() -> {
            userChallengeRepository.updateState(today, UserChallengeStateType.PLAN, UserChallengeStateType.DOING);
        });

//        withTransaction(() -> {
//            // state 가 DOING 에서 END 로 바뀔 예정인 챌린지에 대한 포인트를 계산해준다.
//
//            userChallengeRepository.updateState(today.minusDays(7), UserChallengeStateType.DOING, UserChallengeStateType.END);
//        });

        updateUserChallengeDoingToEnd(today);
    }

    /**
     * user challenge state from doing to end
     *
     * @param date start date
     */
    public void updateUserChallengeDoingToEnd(final LocalDate date) {

        List<UserChallenge> userChallengeList = userChallengeRepository.findAllByStateAndStartDate(UserChallengeStateType.DOING, date.minusDays(7));

        for (UserChallenge userChallenge : userChallengeList) {
            withTransaction(() -> {
                UserChallenge curUserChallenge = userChallengeRepository.findBySeq(userChallenge.getSeq())
                        .orElse(null);

                if (curUserChallenge == null) {
                    // TODO
                }

                //

                if (curUserChallenge.getTotalConfirmCnt() >= curUserChallenge.getChallenge().getRecommendedCnt()) {
                    userRewardRepository.incrementPoint(curUserChallenge.getUser().getSeq(), extraPoints);
                }

                userChallengeRepository.updateUserChallengeState(curUserChallenge.getSeq(), UserChallengeStateType.DOING, UserChallengeStateType.END);
            });
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
