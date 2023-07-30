package com.mozi.moziserver.service;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.AnimalItem;
import com.mozi.moziserver.model.entity.AnimalMention;
import com.mozi.moziserver.repository.AnimalItemRepository;
import com.mozi.moziserver.repository.AnimalMentionRepository;
import com.mozi.moziserver.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalItemRepository animalItemRepository;
    private final AnimalMentionRepository animalMentionRepository;

    public Animal getAnimal(Long seq) {

        return animalRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.ANIMAL_NOT_EXISTS::getResponseException);
    }

    public List<Animal> getAnimalList() {
        return animalRepository.findAll();
    }

    public AnimalItem getAnimalItem(Animal animal, Integer itemTurn) {

        return animalItemRepository.findByAnimalAndTurn(animal, itemTurn).orElseThrow(); // TODO
    }

    public Animal getAnimalByIslandAndTurn(Long islandSeq, Integer turn) {

        return animalRepository.findByIslandSeqAndTurn(islandSeq, turn)
                .orElseThrow(ResponseError.NotFound.ANIMAL_NOT_EXISTS::getResponseException);
    }

    public List<AnimalItem> getAnimalItemList(Animal animal) {

        return animalItemRepository.findAllByAnimal(animal);
    }

    public boolean isLastAnimalInIsland(Animal animal) {
        return animal.getTurn() == getLastAnimalTurnByIsland(animal.getIsland().getSeq());
    }

    public int getLastAnimalTurnByIsland(Long islandSeq) {
        return animalRepository.countByIslandSeq(islandSeq);
    }

    public int getLastItemTurnByIslandAndAnimalTurn(Long islandSeq, Integer animalTurn) {

        if (animalTurn == 0) {
            return 0;
        }

        Animal animal = animalRepository.findByIslandSeqAndTurn(islandSeq, animalTurn)
                .orElseThrow(ResponseError.NotFound.ANIMAL_NOT_EXISTS::getResponseException);
        return animalItemRepository.countByAnimal(animal);
    }

    public int getLastItemTurnByAnimal(Animal animal) {

        return animalItemRepository.countByAnimal(animal);
    }

    public int getNextItemAcquisitionRequiredPoint(Long islandSeq, Integer animalTurn, Integer itemTurn) {

        int maxItemTurn = getLastItemTurnByIslandAndAnimalTurn(islandSeq, animalTurn);

        if (itemTurn == maxItemTurn) {
            Animal animal = getAnimalByIslandAndTurn(islandSeq, animalTurn + 1);
            AnimalItem animalItem = animalItemRepository.findByAnimalAndTurn(animal, 1)
                    .orElseThrow();

            return animalItem.getAcquisitionRequiredPoint();
        }

        Animal animal = getAnimalByIslandAndTurn(islandSeq, animalTurn);
        AnimalItem animalItem = animalItemRepository.findByAnimalAndTurn(animal, itemTurn + 1)
                .orElseThrow();
        return animalItem.getAcquisitionRequiredPoint();
    }

    // -------------------- -------------------- animal mention -------------------- -------------------- //
    public List<String> getAnimalMentionListByAnimalAndItemAndPoint(Long animalSeq, Integer itemTurn, Integer userPoint) {

        List<AnimalMention> animalMentionList = animalMentionRepository.findByAnimalSeqAndItemTurnAndUserWeekPoint(animalSeq, itemTurn, userPoint);

        if (animalMentionList.isEmpty()) {
            return Constant.RANDOM_ANIMAL_MENTIONS;
        }

        return animalMentionList.stream().map(AnimalMention::getContent).collect(Collectors.toList());
    }
}
