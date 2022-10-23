package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Animal;
import lombok.Getter;

@Getter
public class ResAnimal {
    private final Long seq;
    private final String name;
    private final String fullBodyImgUrl;

    private ResAnimal(Animal animal) {
        this.seq = animal.getSeq();
        this.name = animal.getName();
        this.fullBodyImgUrl = animal.getFullBodyImgUrl();
    }

    public static ResAnimal of(Animal animal) {
        return new ResAnimal(animal);
    }
}
