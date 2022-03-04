package com.mozi.moziserver.model.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"challenge", "date", "user"})
public class ChallengeStatisticsUserUniqCheckId implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "challenge_seq")
    private Challenge challenge;

    @Column(name = "start_date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name="user_seq")
    private User user;
}
