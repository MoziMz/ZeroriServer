package com.mozi.moziserver.model.entity;

import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "detail_island")
public class DetailIsland extends AbstractTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer animalTurn;

    private Integer itemTurn;

    private String imgUrl;

    private String thumbnailImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "island_seq")
    Island island;
}
