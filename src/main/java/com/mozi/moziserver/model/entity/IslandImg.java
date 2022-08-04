package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "island_img")
@IdClass(IslandImgId.class)
public class IslandImg extends AbstractTimeEntity {
    @Id
    private Integer type;

    @Id
    private Integer level;

    private String imgUrl;

    private String thumbnailImgUrl;
}
