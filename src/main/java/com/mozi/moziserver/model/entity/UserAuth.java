package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.UserAuthType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "user_auth")
public class UserAuth extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private UserAuthType type;

    private String id;

    private String pw;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private com.mozi.moziserver.model.entity.User user;
}
