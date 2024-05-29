package com.mozi.moziserver.service;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserPointRecord;
import com.mozi.moziserver.model.entity.UserReward;
import com.mozi.moziserver.model.mappedenum.PointReasonType;
import com.mozi.moziserver.repository.UserPointRecordRepository;
import com.mozi.moziserver.repository.UserRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRewardService {
    final UserRewardRepository userRewardRepository;
    final UserPointRecordRepository userPointRecordRepository;

    public UserReward getUserReward(User user) {

        return userRewardRepository.findByUser(user);
    }

    public int getUserPoint(User user) {

        UserReward userReward = userRewardRepository.findByUser(user);
        return userReward.getPoint();
    }

    public void incrementPoint(User user, PointReasonType pointReasonType, Integer point) {

        userRewardRepository.incrementPoint(user.getSeq(), point);
        createUserPointRecord(user, pointReasonType, point);
    }

    public void decrementPoint(User user, PointReasonType pointReasonType, Integer point) {

        userRewardRepository.decrementPoint(user.getSeq(), point);
        createUserPointRecord(user, pointReasonType, -point);
    }

    private void createUserPointRecord(User user, PointReasonType pointReasonType, Integer point) {

        UserPointRecord userPointRecord = UserPointRecord.builder()
                .user(user)
                .point(point)
                .reason(pointReasonType)
                .build();

        userPointRecordRepository.save(userPointRecord);
    }

    public Integer getPointOfUserPointRecordByPeriod(User user, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        Integer sumPoint = 0;
        List<UserPointRecord> userPointRecordList = userPointRecordRepository.findByUserAndPeriod(user, startDateTime, endDateTime);

        for (UserPointRecord userPointRecord : userPointRecordList) {
            if (userPointRecord.getPoint() > 0)
                sumPoint += userPointRecord.getPoint();
        }

        return sumPoint;
    }

    public void createUserRewardForJoin(User user) {

        UserReward userReward = UserReward.builder()
                .user(user)
                .point(Constant.TUTORIAL_ANIMAL_ITEM_POINT)
                .build();
        userRewardRepository.save(userReward);
    }

    public Integer getUserPointOfThisWeek(User user) {
        LocalDateTime now = LocalDateTime.now();

        int minusDays = now.getDayOfWeek().getValue();

        if (minusDays == DayOfWeek.SUNDAY.getValue() && now.getHour() >= 21) {
            minusDays = 0;
        }
        LocalDateTime sundayDate = LocalDateTime.now().minusDays(minusDays).withHour(21).withMinute(0).withSecond(0);

        Integer thisWeekUserPoint = getPointOfUserPointRecordByPeriod(user, sundayDate, now);

        return thisWeekUserPoint;
    }

}

