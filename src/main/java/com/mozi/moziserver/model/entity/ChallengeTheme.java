package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "challenge_theme")
public class ChallengeTheme extends AbstractTimeEntity {

    @EmbeddedId
    private ChallengeThemeId id;
}
