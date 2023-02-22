package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.EmailAuthType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "email_check_auth")
public class EmailCheckAuth extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private EmailAuthType type;

    private String token;

    private String email;

    private LocalDateTime usedDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private com.mozi.moziserver.model.entity.User user;
}