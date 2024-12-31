package com.mozi.moziserver.model.entity;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "animal_item")
public class AnimalItem extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer turn;

    private String name;

    private Integer acquisitionRequiredPoint;

    private String colorImgUrl;

    private String blackImgUrl;

    @Transient
    boolean isAcquisition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_seq")
    private Animal animal;
}
