package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "postbox_message_animal")
public class PostboxMessageAnimal extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private boolean checkedState;

    @Builder.Default
    private Integer level = 1; // TODO ERASE V2

    @Transient
    List<AnimalItem> animalItemList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_seq")
    private Animal animal;
}
