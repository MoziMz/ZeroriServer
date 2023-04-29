package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.UserNoticeType;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.repository.PostboxMessageAnimalItemRepository;
import com.mozi.moziserver.repository.PostboxMessageAnimalRepository;
import com.mozi.moziserver.repository.UserNoticeRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostboxMessageAnimalService {

    private final UserRepository userRepository;
    private final PostboxMessageAnimalRepository postboxMessageAnimalRepository;
    private final PostboxMessageAnimalItemRepository postboxMessageAnimalItemRepository;
    private final UserNoticeService userNoticeService;
    private final UserNoticeRepository userNoticeRepository;
    private final AnimalService animalService;

    private final FcmService fcmService;

    // -------------------- -------------------- PostboxMessageAnimal -------------------- -------------------- //
    public PostboxMessageAnimal getPostboxMessageAnimal(Long userSeq, Long seq) {

        User user = userRepository.getById(userSeq);

        PostboxMessageAnimal postboxMessageAnimal = postboxMessageAnimalRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POSTBOX_MESSAGE_ANIMAL_NOT_EXISTS::getResponseException);

        Set<Long> acquisitionAnimalItemSeqSet =
                postboxMessageAnimalItemRepository.findAllByPostboxMessageAnimal(postboxMessageAnimal)
                        .stream().map(p -> p.getAnimalItem().getSeq())
                        .collect(Collectors.toSet());

        List<AnimalItem> animalItemList = animalService.getAnimalItemList(postboxMessageAnimal.getAnimal());
        for (AnimalItem item : animalItemList) {
            item.setAcquisition(acquisitionAnimalItemSeqSet.contains(item.getSeq()));
        }

        postboxMessageAnimal.setAnimalItemList(animalItemList);

        return postboxMessageAnimal;
    }
    // 해당 유저의 레벨 별 동물 정보
    // 프론트에서) 우선, 아래의 흑백이미지를 배경으로 불려오고, 성공하면 이 리스트를 덧붙여서 보여준다.

    // 동물의 준비물 리스트
//    public List<PreparationItem> getItemList(Long userSeq, Long animalSeq) {
//        User user = userRepository.findById(userSeq)
//                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);
//
//        return postboxMessageAnimalRepository.findItemByUser(user, animalSeq);
//    }

    public List<PostboxMessageAnimal> getPostboxMessageAnimalList(Long userSeq, ReqList req) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return postboxMessageAnimalRepository.findAllByUser(user, req.getPageSize(), req.getPrevLastSeq());
    }

    public PostboxMessageAnimal getRecentPostboxMessageAnimalByUser(User user) {

        return postboxMessageAnimalRepository.findLastOneByUser(user);

    }

    @Transactional
    public void createPostboxMessageAnimal(User user, Animal animal) {

        PostboxMessageAnimal postboxMessageAnimal = PostboxMessageAnimal.builder()
                .user(user)
                .animal(animal)
                .checkedState(false)
                .build();

        postboxMessageAnimalRepository.save(postboxMessageAnimal);

        userNoticeService.upsertUserNotice(user, UserNoticeType.POSTBOX_MESSAGE_ANIMAL_NEW_ARRIVED, postboxMessageAnimal.getSeq());
    }

    public void createFirstMessageInIsland(User user, Long islandSeq) {

        Animal animal = animalService.getAnimalByIslandAndTurn(islandSeq, 1);
        createPostboxMessageAnimal(user, animal);
    }

    public void checkMessage(Long seq) {

        PostboxMessageAnimal postboxMessageAnimal = postboxMessageAnimalRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POSTBOX_MESSAGE_ANIMAL_NOT_EXISTS::getResponseException);
        postboxMessageAnimal.setCheckedState(true);
        postboxMessageAnimalRepository.save(postboxMessageAnimal);
    }

    // -------------------- -------------------- PostboxMessageAnimalItem -------------------- -------------------- //
    public List<PostboxMessageAnimalItem> getPostBoxMessageAnimalItemList(PostboxMessageAnimal postboxMessageAnimal) {

        return postboxMessageAnimalItemRepository.findAllByPostboxMessageAnimal(postboxMessageAnimal);
    }

    // -------------------- -------------------- UserNotice -------------------- -------------------- //
    public UserNotice getUserNoticeByUserAndType(Long userSeq, UserNoticeType userNoticeType) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return userNoticeRepository.findOneByUserAndTypeAndCheckedState(user, userNoticeType)
                .orElseThrow(ResponseError.NotFound.USER_NOTICE_NOT_EXISTS::getResponseException);
    }

    @Transactional
    public void checkUserNotice(Long userSeq, UserNoticeType type) {

        UserNotice userNotice = getUserNoticeByUserAndType(userSeq, type);

        userNotice.setCheckedState(true);

        userNoticeRepository.save(userNotice);
    }

    // -------------------- -------------------- ETC -------------------- -------------------- //
    // return void new Arrived postboxMessageAnimal
    @Transactional
    public int incrementPostboxMessageAnimalItem(User user) {

        PostboxMessageAnimal postboxMessageAnimal = postboxMessageAnimalRepository.findLastOneByUser(user);
        int nextItemTurn = postboxMessageAnimalItemRepository.findAllByPostboxMessageAnimal(postboxMessageAnimal).size() + 1;
        AnimalItem nextAnimalItem = animalService.getAnimalItem(postboxMessageAnimal.getAnimal(), nextItemTurn);

        PostboxMessageAnimalItem postboxMessageAnimalItem = PostboxMessageAnimalItem.builder()
                .animalItem(nextAnimalItem)
                .postboxMessageAnimal(postboxMessageAnimal)
                .build();
        postboxMessageAnimalItemRepository.save(postboxMessageAnimalItem);
        userNoticeService.upsertUserNotice(user, UserNoticeType.POSTBOX_MESSAGE_ANIMAL_RECEIVED_ITEM, postboxMessageAnimal.getSeq());

        postboxMessageAnimal.setLevel(postboxMessageAnimal.getLevel() + 1); // TODO ERASE V2
        postboxMessageAnimal.setCheckedState(false);
        postboxMessageAnimalRepository.save(postboxMessageAnimal);

        int lastItemTurn = animalService.getLastItemTurnByAnimal(postboxMessageAnimal.getAnimal());
        if (nextItemTurn == lastItemTurn
                && !animalService.isLastAnimalInIsland(postboxMessageAnimal.getAnimal())) {
            Animal nextAnimal = animalService.getAnimalByIslandAndTurn(
                    postboxMessageAnimal.getAnimal().getIsland().getSeq(),
                    postboxMessageAnimal.getAnimal().getTurn() + 1);
            createPostboxMessageAnimal(user, nextAnimal);
            return 1;
        }

        return 0;
    }
}
