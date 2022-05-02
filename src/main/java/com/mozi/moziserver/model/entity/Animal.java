package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.AnimalType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "animal")
public class Animal extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Enumerated(EnumType.STRING)
    private AnimalType animalName;

    private String content;

    private String imgUrl;

}
