package com.mozi.moziserver.service;


import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserIsland;
import com.mozi.moziserver.repository.IslandRepository;
import com.mozi.moziserver.repository.UserIslandRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserIslandService {
    private final UserRepository userRepository;
    private final IslandRepository islandRepository;
    private final UserIslandRepository userIslandRepository;

    public List<UserIsland> getUserIslandList(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return userIslandRepository.findAllByUser(user);
    }

    public List<Island> getIslandList() {
        return islandRepository.findAllByOrderByTypeAsc();
    }
}
