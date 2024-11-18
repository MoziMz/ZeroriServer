package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.AnimalMentionFcmMessage;
import com.mozi.moziserver.adminService.AdminConfirmService;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.ConfirmStateType;
import com.mozi.moziserver.model.mappedenum.FcmMessageType;
import com.mozi.moziserver.repository.IslandRepository;
import com.mozi.moziserver.repository.UserIslandRepository;
import com.mozi.moziserver.model.req.ReqConfirmCheck;
import com.mozi.moziserver.rest.GithubModelsClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.mozi.moziserver.common.Constant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncService {

    private final UserRewardService userRewardService;
    private final AnimalService animalService;
    private final PostboxMessageAnimalService postboxMessageAnimalService;
    private final FcmService fcmService;
    private final GithubModelsClient githubModelsClient;
    private final AdminConfirmService adminConfirmService;

    private final IslandRepository islandRepository;
    private final UserIslandRepository userIslandRepository;

    @Async
    public void sendNewAnimalNotification(User user) {

        //저번주 일요일부터 현재까지 모은 포인트
        Integer thisWeekUserRewardPoint = userRewardService.getUserPointOfThisWeek(user);

        //유저의 마지막 PostBoxMessageAnimal을 얻어서 AnimalItem의 필요포인트 얻기
        PostboxMessageAnimal postboxMessageAnimal = postboxMessageAnimalService.getRecentPostboxMessageAnimalByUser(user);

        List<PostboxMessageAnimalItem> postboxMessageAnimalItemList = postboxMessageAnimalService.getPostBoxMessageAnimalItemList(postboxMessageAnimal);

        Integer requiredPoints = 0;
        List<AnimalItem> animalItems = animalService.getAnimalItemList(postboxMessageAnimal.getAnimal());

        //아이템이 0개이거나 마지막 아이템이 필요한게 아니라면 알림 보내지 않음
        if (postboxMessageAnimalItemList.isEmpty() || postboxMessageAnimalItemList.size() + 1 != animalItems.size()) {
            return;
        }

        //유저의 현재 섬이 마지막 단계이면 알림 보내지 않음
        UserIsland lastUserIsland = getLastUserIsland(user);
        DetailIsland lastDetailIsland = lastUserIsland.getDetailIsland();
        if (isLastDetailIslandOfIsland(lastDetailIsland)) {
            return;
        }

        //AnimalItem 리스트중에서 마지막 아이템의 포인트 얻기
        requiredPoints = animalItems.stream().max(Comparator.comparing(AnimalItem::getTurn))
                .orElseThrow().getAcquisitionRequiredPoint();

        //이삿날 푸시 알림 보내기
        if (thisWeekUserRewardPoint >= requiredPoints) {
            LocalDate now = LocalDate.now();
            LocalDate movingDate = now.minusDays(now.getDayOfWeek().getValue()).plusDays(7);
            fcmService.sendDateMessageToUser(user, movingDate, FcmMessageType.MOVING_DAY_OF_NEW_ANIMAL);
        }
    }

    // -------------------- -------------------- animal mention -------------------- --------------------
    @Async
    public void sendAnimalMention(User user) {

        // 저번주 일요일부터 현재까지 모은 포인트
        Integer thisWeekUserRewardPoint = userRewardService.getUserPointOfThisWeek(user);

        // 섬의 마지막 단계면 보내지 않음
        UserIsland lastUserIsland = getLastUserIsland(user);
        DetailIsland lastDetailIsland = lastUserIsland.getDetailIsland();
        if (isLastDetailIslandOfIsland(lastDetailIsland)) {
            return;
        }

        // 유저의 일주일 획득 포인트가 리셋되는 날로 expirationDateTime 초기화하기
        // 만약 현재가 일요일인데 21시 이후이면 다가오는 일요일 21시로
        // 만약 현재가 일요일인데 21시 이전이면 오늘 일요일 21시로
        FcmMessageType fcmMessageType = FcmMessageType.ANIMAL_MENTION;
        LocalDateTime expirationDateTime = userRewardService.getNextResetTimeOfUserWeekPoint();

        if (lastDetailIsland.getItemTurn() == animalService.getLastItemTurnByIslandAndAnimalTurn(lastDetailIsland.getIsland().getSeq(), lastDetailIsland.getAnimalTurn())) {

            // 1. 현재 단계의 동물이 마지막 단계이면(아이템을 모두 가지고 있으면) 다음 동물의 멘션 보내기
            // 다음 단계 동물 조회 -> 해당 동물과 유저 포인트에 따른 메세지 보내기
            Animal currentAnimal = animalService.getAnimalByIslandAndTurn(lastDetailIsland.getIsland().getSeq(), lastDetailIsland.getAnimalTurn());

            List<String> animalMentions = animalService.getAnimalMentionListByAnimalAndItemAndPoint(currentAnimal.getSeq(), lastDetailIsland.getItemTurn(), thisWeekUserRewardPoint);

            AnimalMentionFcmMessage animalMentionMessage = AnimalMentionFcmMessage.builder()
                    .fcmMessageType(fcmMessageType)
                    .expirationDateTime(expirationDateTime)
                    .islandSeq(lastDetailIsland.getSeq())
                    .animalImgUrl(currentAnimal.getImgUrl())
                    .mentionList(animalMentions)
                    .build();

            fcmService.sendMessageToUser(user, animalMentionMessage);
        } else {

            // 2. 현재 단계 동물의 마지막 단계가 아니면 현재 동물의 멘션 보내기
            // 현재 동물 조회 -> 해당 동물과 유저 포인트에 따른 메세지 보내기
            Animal nextAnimal = animalService.getAnimalByIslandAndTurn(lastDetailIsland.getIsland().getSeq(), lastDetailIsland.getAnimalTurn() + 1);

            List<String> animalMentions = animalService.getAnimalMentionListByAnimalAndItemAndPoint(nextAnimal.getSeq(), lastDetailIsland.getItemTurn(), thisWeekUserRewardPoint);

            AnimalMentionFcmMessage animalMentionMessage = AnimalMentionFcmMessage.builder()
                    .fcmMessageType(fcmMessageType)
                    .expirationDateTime(expirationDateTime)
                    .islandSeq(lastDetailIsland.getSeq())
                    .animalImgUrl(nextAnimal.getImgUrl())
                    .mentionList(animalMentions)
                    .build();

            fcmService.sendMessageToUser(user, animalMentionMessage);
        }
    }

    private UserIsland getLastUserIsland(User user) {

        return userIslandRepository.findTopByUserOrderByIsland(user);
    }

    private boolean isLastDetailIslandOfIsland(DetailIsland detailIsland) {

        Island island = islandRepository.findById(detailIsland.getIsland().getSeq())
                .orElseThrow(ResponseError.NotFound.ISLAND_NOT_EXISTS::getResponseException);

        int lastAnimalTurn = animalService.getLastAnimalTurnByIsland(island.getSeq());
        int LastItemTurn = animalService.getLastItemTurnByIslandAndAnimalTurn(
                detailIsland.getIsland().getSeq(), lastAnimalTurn);

        return lastAnimalTurn == detailIsland.getAnimalTurn() && LastItemTurn == detailIsland.getItemTurn();
    }


    @Async
    void verifyConfirm(Long confirmSeq, String imgUrl, String challengeName) {
        String formattedText = String.format(GITHUB_MODELS_CHALLENGE_PROMPT, challengeName);

        ReqConfirmCheck reqConfirmCheck = ReqConfirmCheck.builder()
                .maxTokens(GITHUB_MODELS_MAX_TOKENS)
                .model(GITHUB_MODELS_MODEL_NAME)
                .messages(List.of(ReqConfirmCheck.Message.builder()
                        .role(GITHUB_MODELS_ROLE)
                        .contentList(List.of(ReqConfirmCheck.TextContent.builder()
                                        .text(formattedText)
                                        .build(),
                                ReqConfirmCheck.ImageContent.builder()
                                        .imageUrl(ReqConfirmCheck.ImageUrl.builder()
                                                .url(imgUrl)
                                                .build())
                                        .build()))
                        .build()))
                .build();

        Optional<GithubModelsClient.ResConfirmCheck> resConfirmCheck;
        try {
            resConfirmCheck = Optional.of(githubModelsClient.verifyConfirmForChallengeName(reqConfirmCheck));
        } catch (FeignException e) {
            resConfirmCheck = Optional.empty();
            log.error(e.getMessage(), e);
        }

        resConfirmCheck.ifPresent(res -> {
            //제로 챌린지에 알맞지 않은 인증 이미지이면 BLOCKED 처리
            if (res.isAllContentN()) {
                adminConfirmService.updateConfirmState(confirmSeq, ConfirmStateType.BLOCKED);
            }
        });
    }
}
