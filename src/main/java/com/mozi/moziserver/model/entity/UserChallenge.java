package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.UserChallengeResult;
import com.mozi.moziserver.model.mappedenum.ResultListConverter;
import com.mozi.moziserver.model.mappedenum.UserChallengeResultType;
import com.mozi.moziserver.model.mappedenum.UserChallengeStateType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Entity(name = "user_challenge")
public class UserChallenge extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Enumerated(EnumType.STRING)
    private UserChallengeStateType state;

    private LocalDate startDate;

    @Builder.Default
    @Convert(converter = ResultListConverter.class)
    private List<UserChallengeResult> resultList = IntStream.range(1, 7+1)
            .mapToObj(turn -> UserChallengeResult.builder()
                    .turn(turn)
                    .result(UserChallengeResultType.NONE)
                    .build())
            .collect(Collectors.toList());

    private Integer totalConfirmCnt;

    @Builder.Default
    private Integer acquisitionPoints = 0;

    private boolean checkedState;

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
