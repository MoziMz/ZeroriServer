package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "island")
public class Island extends AbstractTimeEntity {

    @Id
    private Long seq;

    private String name;

    private String description;

    private Integer openRequiredPoint;
}
