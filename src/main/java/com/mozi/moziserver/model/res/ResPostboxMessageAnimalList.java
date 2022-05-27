package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResPostboxMessageAnimalList {
    private Long seq;
    private String animalName;
    private String content;
    private boolean checkedState;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ResPostboxMessageAnimalList (PostboxMessageAnimal postboxMessageAnimal) {
        this.seq = postboxMessageAnimal.getSeq();
        this.animalName = postboxMessageAnimal.getAnimal().getName();
        this.content = postboxMessageAnimal.getContent();
        this.checkedState = postboxMessageAnimal.isCheckedState();
        this.createdAt = postboxMessageAnimal.getCreatedAt();
        this.updatedAt = postboxMessageAnimal.getUpdatedAt();
    }

    public static ResPostboxMessageAnimalList of (PostboxMessageAnimal postboxMessageAnimal) {
        return new ResPostboxMessageAnimalList(postboxMessageAnimal);
    }
}
