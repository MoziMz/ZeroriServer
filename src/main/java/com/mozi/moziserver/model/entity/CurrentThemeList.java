package com.mozi.moziserver.model.entity;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "current_theme_list")
public class CurrentThemeList extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer turn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_theme_seq")
    private ChallengeTheme challengeTheme;
}
