package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.PlanDate;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import com.mozi.moziserver.model.mappedenum.PlanDateListConverter;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_challenge")
public class UserChallenge extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Enumerated(EnumType.STRING)
    private UserChallengeStateType state;

    private LocalDate startDate;

    @Convert(converter = PlanDateListConverter.class)
    private List<PlanDate> PlanDateList;

    private Integer totalConfirmCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_seq")
    private Challenge challenge;

    @PrePersist
    public void prePersist() {
        this.totalConfirmCnt = Optional.ofNullable(this.totalConfirmCnt).orElse(0);
    }
}
