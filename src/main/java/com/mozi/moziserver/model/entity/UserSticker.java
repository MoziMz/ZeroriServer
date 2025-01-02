package com.mozi.moziserver.model.entity;

import lombok.*;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_sticker")
public class UserSticker extends AbstractTimeEntity {

    @EmbeddedId
    private UserStickerId id;
}
