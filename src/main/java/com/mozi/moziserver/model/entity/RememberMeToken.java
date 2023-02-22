package com.mozi.moziserver.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "remember_me_token")
public class RememberMeToken extends AbstractTimeEntity {

    @Id
    private String series;

    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
}
