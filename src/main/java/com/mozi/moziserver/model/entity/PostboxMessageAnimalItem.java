package com.mozi.moziserver.model.entity;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "postbox_message_animal_item")
public class PostboxMessageAnimalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postbox_message_animal_seq")
    private PostboxMessageAnimal postboxMessageAnimal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_item_seq")
    private AnimalItem animalItem;
}
