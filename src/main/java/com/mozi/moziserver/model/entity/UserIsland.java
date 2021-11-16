package com.mozi.moziserver.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "user_island")
public class UserIsland extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer type;

    @Column(name = "reward_level")
    private Integer rewardLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
            @JoinColumn(name = "type", insertable = false, updatable = false),
            @JoinColumn(name = "reward_level", insertable = false, updatable = false)
    })
    private IslandReward islandReward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", insertable = false, updatable = false)
    private IslandType islandType;
}
