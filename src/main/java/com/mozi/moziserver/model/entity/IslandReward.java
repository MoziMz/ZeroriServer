package com.mozi.moziserver.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "island_reward")
@IdClass(IslandRewardId.class)
public class IslandReward extends AbstractTimeEntity {
    @Id
    private Integer type;

    @Id
    private Integer level;

    private String imgUrl;
}
