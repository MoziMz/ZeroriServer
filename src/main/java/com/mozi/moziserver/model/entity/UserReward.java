package com.mozi.moziserver.model.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_reward")
public class UserReward extends AbstractTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Builder.Default
    private Integer point = 0;

    @Builder.Default
    private Integer bRewardCnt = 0;

    @Builder.Default
    private Integer cRewardCnt = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
}
