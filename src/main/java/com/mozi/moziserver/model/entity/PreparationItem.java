package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "preparation_item")
public class PreparationItem extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="animal_seq")
    private Animal animal;

    private Integer turn;

    private String name;

    private String colorImgUrl;

    private String blackImgUrl;
}
