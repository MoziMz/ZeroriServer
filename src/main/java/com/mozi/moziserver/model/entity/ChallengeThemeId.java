package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.ChallengeThemeType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"themeName","challenge"})
public class ChallengeThemeId implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(name="theme_name")
    private ChallengeThemeType themeName;

    @ManyToOne
    @JoinColumn(name="challenge_seq")
    Challenge challenge;
}