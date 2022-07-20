package com.mozi.moziserver.service;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserPointRecord;
import com.mozi.moziserver.model.entity.UserReward;
import com.mozi.moziserver.model.mappedenum.PointReasonType;
import com.mozi.moziserver.repository.UserPointRecordRepository;
import com.mozi.moziserver.repository.UserRepository;
import com.mozi.moziserver.repository.UserRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void createUserPointRecord(User user, PointReasonType pointReasonType, Integer point){

        UserPointRecord userPointRecord=UserPointRecord.builder()
                    .user(user)
                    .point(point)
                    .reason(pointReasonType)
                    .build();

        userPointRecordRepository.save(userPointRecord);
    }

}

