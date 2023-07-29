package com.mozi.moziserver.service;


import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.DetailIsland;
import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserIsland;
import com.mozi.moziserver.model.mappedenum.PointReasonType;
import com.mozi.moziserver.repository.DetailIslandRepository;
import com.mozi.moziserver.repository.IslandRepository;
import com.mozi.moziserver.repository.UserIslandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IslandService {
    
    private final UserRewardService userRewardService;
    private final PostboxMessageAnimalService postboxMessageAnimalService;
    private final AnimalService animalService;
    private final AsyncService asyncService;
    
    private final IslandRepository islandRepository;
    private final UserIslandRepository userIslandRepository;
    private final DetailIslandRepository detailIslandRepository;

    // -------------------- -------------------- island -------------------- -------------------- //
    private Island getIsland(Long seq) {

        return islandRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.ISLAND_NOT_EXISTS::getResponseException);
    }
    
    public List<Island> getIslandListOrderBySeq() {

        return islandRepository.findAllByOrderBySeqAsc();
    }

    // -------------------- -------------------- detail island -------------------- -------------------- //
    private DetailIsland getDetailIsland(Long islandSeq, Integer animalTurn, Integer itemTurn) {

        return detailIslandRepository.findByIslandSeqAndAnimalTurnAndItemTurn(islandSeq, animalTurn, itemTurn)
                .orElseThrow(ResponseError.NotFound.DETAIL_ISLAND_NOT_EXISTS::getResponseException);
    }

    public DetailIsland getFirstDetailIslandOfIsland(Long islandSeq) {

        return detailIslandRepository.findByIslandSeqAndAnimalTurnAndItemTurn(islandSeq, 0, 0)
                .orElseThrow(ResponseError.NotFound.DETAIL_ISLAND_NOT_EXISTS::getResponseException);
    }

    public boolean isLastDetailIslandOfIsland(DetailIsland detailIsland) {

        Island island = getIsland(detailIsland.getIsland().getSeq());

        int lastAnimalTurn = animalService.getLastAnimalTurnByIsland(island.getSeq());
        int LastItemTurn = animalService.getLastItemTurnByIslandAndAnimalTurn(
                detailIsland.getIsland().getSeq(), lastAnimalTurn);

        return lastAnimalTurn == detailIsland.getAnimalTurn() && LastItemTurn == detailIsland.getItemTurn();
    }

    public boolean isLastDetailIslandOfWorld(DetailIsland detailIsland) {

        if (detailIsland.getIsland().getSeq() != Constant.lastIslandSeq) {
            return false;
        }
        int lastAnimalTurn = animalService.getLastAnimalTurnByIsland(detailIsland.getIsland().getSeq());
        int LastItemTurn = animalService.getLastItemTurnByIslandAndAnimalTurn(
                detailIsland.getIsland().getSeq(), lastAnimalTurn);

        return lastAnimalTurn == detailIsland.getAnimalTurn() && LastItemTurn == detailIsland.getItemTurn();
    }

    // -------------------- -------------------- user island -------------------- -------------------- //
    public UserIsland getLastUserIsland(User user) {

        return userIslandRepository.findTopByUserOrderByIsland(user);
    }
    
    public List<UserIsland> getUserIslandListOrderByIslandSeq(User user) {

        return userIslandRepository.findAllByUserOrderByIsland(user);
    }

    public void firstCreateUserIsland(User user) {

        createUserIsland(user, 1L);
    }

    public void createUserIsland(User user, Long islandSeq) {

        DetailIsland detailIsland = getFirstDetailIslandOfIsland(islandSeq);
        UserIsland userIsland = UserIsland.builder()
                .detailIsland(detailIsland)
                .user(user)
                .build();
        userIslandRepository.save(userIsland);

        postboxMessageAnimalService.createFirstMessageInIsland(user, islandSeq);

        asyncService.sendNewAnimalNotification(user);
        asyncService.sendAnimalMention(user);
    }

    @Transactional
    public void openUserIsland(User user) {

        DetailIsland lastDetailIsland = userIslandRepository.findTopByUser(user).getDetailIsland();

        // step1. 유저가 현재 섬의 마지막 레벨에 도달했는지
        if (!isLastDetailIslandOfIsland(lastDetailIsland)) {
            throw ResponseError.BadRequest.INVALID_USER_ISLAND_OPEN.getResponseException();
        }

        // step2. 마지막 단계 섬인지
        if (isLastDetailIslandOfWorld(lastDetailIsland)) {
            throw ResponseError.BadRequest.INVALID_USER_ISLAND_OPEN.getResponseException();
        }

        // step3. 유저가 다음 섬의 오픈 포인트를 충족하는지
        Island nextIsland = islandRepository.getById(lastDetailIsland.getIsland().getSeq() + 1L);
        int userPoint = userRewardService.getUserPoint(user);
        if (userPoint < nextIsland.getOpenRequiredPoint()) {
            throw ResponseError.BadRequest.INVALID_USER_ISLAND_OPEN.getResponseException();
        }

        // step4. 다음 섬 오픈 (섬 오픈, 포인트 차감)
        createUserIsland(user, nextIsland.getSeq());
        userRewardService.decrementPoint(user, PointReasonType.ISLAND_OPEN, nextIsland.getOpenRequiredPoint());
    }

    @Transactional
    public void upgradeUserIsland(UserIsland userIsland, DetailIsland detailIsland) {

        Long islandSeq = detailIsland.getIsland().getSeq();
        Integer animalTurn = detailIsland.getAnimalTurn();
        Integer itemTurn = detailIsland.getItemTurn();

        int maxItemTurnOfAnimal = animalService.getLastItemTurnByIslandAndAnimalTurn(islandSeq, animalTurn);
        DetailIsland nextDetailIsland;
        if (itemTurn == maxItemTurnOfAnimal) {
            nextDetailIsland = detailIslandRepository.findByIslandSeqAndAnimalTurnAndItemTurn(
                    islandSeq, animalTurn + 1, 1).orElse(null);
        } else {
            nextDetailIsland = detailIslandRepository.findByIslandSeqAndAnimalTurnAndItemTurn(
                    islandSeq, animalTurn, itemTurn + 1).orElseThrow(null);
        }
        userIsland.setDetailIsland(nextDetailIsland);
        userIslandRepository.save(userIsland);
    }
}