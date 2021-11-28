package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "challenge_tag")
public class ChallengeTag extends AbstractTimeEntity {

    @EmbeddedId
    private ChallengeTagId id;
}
