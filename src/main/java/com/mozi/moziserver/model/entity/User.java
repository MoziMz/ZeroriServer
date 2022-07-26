package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.common.UserState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "user")
public class User extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String nickName;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserState state = UserState.ACTIVE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seq")
    UserReward userReward;
}
