package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "island")
public class Island extends AbstractTimeEntity {
    @Id
    private Integer type;

    private String name;

    private Integer maxPoint;

    private Integer maxRewardLevel;

    private String description;
}
