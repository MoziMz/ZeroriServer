package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.PostboxMessageAdmin;
import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.PreparationItem;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.repository.PostboxMessageAnimalRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostboxMessageAnimalService {
    private final UserRepository userRepository;
    private final PostboxMessageAnimalRepository postboxMessageAnimalRepository;
    public PostboxMessageAnimal getPostboxMessageAnimal(Long userSeq, Long seq) {
        PostboxMessageAnimal postboxMessageAnimal = postboxMessageAnimalRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POSTBOX_MESSAGE_ANIMAL_NOT_EXISTS::getResponseException);

        if (!postboxMessageAnimal.getUser().getSeq().equals(userSeq)) {
            throw ResponseError.Forbidden.NO_AUTHORITY.getResponseException();
        }

        return postboxMessageAnimal;
    }
    // 해당 유저의 레벨 별 동물 정보
    // 프론트에서) 우선, 아래의 흑백이미지를 배경으로 불려오고, 성공하면 이 리스트를 덧붙여서 보여준다.

    // 동물의 준비물 리스트
    public List<PreparationItem> getItemList(Long userSeq, Long animalSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return postboxMessageAnimalRepository.findItemByUser(user, animalSeq);
    }

    public List<PostboxMessageAnimal> getPostboxMessageAnimalList(Long userSeq, ReqList req) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return postboxMessageAnimalRepository.findAllByUser(user, req.getPageSize(), req.getPrevLastSeq());
    }

    @Transactional
    public void checkMessage(Long userSeq, Long seq) {
        final PostboxMessageAnimal postboxMessageAnimal = getPostboxMessageAnimal(userSeq, seq);
        postboxMessageAnimal.setCheckedState(true);

        postboxMessageAnimalRepository.save(postboxMessageAnimal);
    }
}
