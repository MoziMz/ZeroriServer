package com.mozi.moziserver.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "island_type")
public class IslandType extends AbstractTimeEntity {
    @Id
    private Integer type;

    private String name;

    private Integer maxRewardLevel;

}
