package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_sticker")
public class UserSticker extends AbstractTimeEntity{
    @EmbeddedId
    private UserStickerId id;
}
