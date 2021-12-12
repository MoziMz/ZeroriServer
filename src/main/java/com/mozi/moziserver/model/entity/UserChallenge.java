package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "user_challenge")
public class UserChallenge extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Enumerated(EnumType.STRING)
    private UserChallengeStateType state;

    private LocalDate startDate;

    private String planDate;

    private Integer totalConfirmCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_seq")
    private Challenge challenge;
}
