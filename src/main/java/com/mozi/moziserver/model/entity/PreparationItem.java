package com.mozi.moziserver.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "preparation_item")
@IdClass(PreparationItemId.class)
public class PreparationItem extends AbstractTimeEntity {
    @Id
    private Long animalSeq;

    @Id
    private Integer turn;

    private String colorImgUrl;

    private String blackImgUrl;
}
