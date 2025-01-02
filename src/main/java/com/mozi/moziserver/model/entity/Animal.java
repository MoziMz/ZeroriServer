package com.mozi.moziserver.model.entity;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "animal")
public class Animal extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer turn;

    private String name;

    private String imgUrl;

    private String fullBodyImgUrl;

    private String postboxAnimalContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "island_seq")
    Island island;
}
