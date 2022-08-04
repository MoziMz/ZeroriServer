package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.EmailAuthType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "email_auth")
public class EmailAuth extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private EmailAuthType type;

    private String token;

    private String id;

    private String pw;

    private LocalDateTime usedDt;

    private boolean checkedState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private com.mozi.moziserver.model.entity.User user;
}