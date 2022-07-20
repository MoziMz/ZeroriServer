package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @JoinColumn(name = "type", insertable = false, updatable = false)
    private Island island;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="type", referencedColumnName="type", insertable = false, updatable = false),
            @JoinColumn(name="reward_level", referencedColumnName="level", insertable = false, updatable = false)
    })
    private IslandImg islandImg;
}
