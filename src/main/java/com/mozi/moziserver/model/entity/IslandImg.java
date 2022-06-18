package com.mozi.moziserver.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "island_img")
@IdClass(IslandImgId.class)
public class IslandImg extends AbstractTimeEntity {
    @Id
    private Integer type;

    @Id
    private Integer level;

    private String imgUrl;
}
