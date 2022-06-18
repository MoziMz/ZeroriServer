package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "preparation_item")
@IdClass(PreparationItemId.class)
public class PreparationItem extends AbstractTimeEntity {
    @Id
    private Long animalSeq;

    @Id
    private Integer turn;

    private String name;

    private String colorImgUrl;

    private String blackImgUrl;
}
