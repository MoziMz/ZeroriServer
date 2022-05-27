package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.AnimalType;
import lombok.*;

import javax.persistence.*;

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

    private String name;

    private String explanation;

    private String imgUrl;
}
