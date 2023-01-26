package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserPointRecord;
import com.mozi.moziserver.model.entity.UserReward;
import com.mozi.moziserver.model.mappedenum.PointReasonType;
import com.mozi.moziserver.repository.UserPointRecordRepository;
import com.mozi.moziserver.repository.UserRepository;
import com.mozi.moziserver.repository.UserRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRewardService {
    final UserRewardRepository userRewardRepository;
    final UserRepository userRepository;

    final UserPointRecordRepository userPointRecordRepository;

    public UserReward getUserReward(Long userSeq) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return userRewardRepository.findByUser(user);
    }
    public int getUserPoint(Long userSeq) {
        User user = userRepository.getById(userSeq);
        UserReward userReward = userRewardRepository.findByUser(user);
        return userReward.getPoint();
    }

    public void createUserPointRecord(User user, PointReasonType pointReasonType, Integer point){

        UserPointRecord userPointRecord=UserPointRecord.builder()
                    .user(user)
                    .point(point)
                    .reason(pointReasonType)
                    .build();

        userPointRecordRepository.save(userPointRecord);
    }

    public Integer getPointOfUserPointRecord(User user, LocalDateTime startDateTime, LocalDateTime endDateTime){

        Integer sumPoint=0;
        List<UserPointRecord> userPointRecordList=userPointRecordRepository.findByUserAndPeriod(user,startDateTime,endDateTime);

        for(UserPointRecord userPointRecord: userPointRecordList){
            if(userPointRecord.getPoint()>0)
                sumPoint+=userPointRecord.getPoint();
        }

        return sumPoint;
    }

    public void firstCreateUserReward(User user){

        UserReward userReward = UserReward.builder()
                .user(user)
                .point(0)
                .build();
        userRewardRepository.save(userReward);
    }

}

