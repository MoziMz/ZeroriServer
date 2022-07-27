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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRewardService {
    final UserRewardRepository userRewardRepository;
    final UserRepository userRepository;

    final UserPointRecordRepository userPointRecordRepository;

    public UserReward getUserReward(Long userSeq) {
        User user = userRepository.getById(userSeq);
        return userRewardRepository.findByUser(user);
    }
    public int getUserPoint(Long userSeq) {
        User user = userRepository.getById(userSeq);
        UserReward userReward = userRewardRepository.findByUser(user);
        return userReward.getPoint();
    }

    @Transactional
    public void createUserPointRecord(User user, PointReasonType pointReasonType, Integer point){

        UserPointRecord userPointRecord=UserPointRecord.builder()
                    .user(user)
                    .point(point)
                    .reason(pointReasonType)
                    .build();

        userPointRecordRepository.save(userPointRecord);
    }

    @Transactional
    public Integer getPointOfUserPointRecord(User user, LocalDateTime startDateTime, LocalDateTime endDateTime){

        Integer sumPoint=0;
        List<UserPointRecord> userPointRecordList=userPointRecordRepository.findByUserAndDate(user,startDateTime,endDateTime);

        for(UserPointRecord userPointRecord: userPointRecordList){
            if(userPointRecord.getPoint()>0)
                sumPoint+=userPointRecord.getPoint();
        }

        return sumPoint;
    }

}

