package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.swing.text.StyledEditorKit;

/**
 * uniq(deviceId, token)
 * 멀티 로그인 가능
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_fcm")
public class UserFcm extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String deviceId;

    private String token;

    private Boolean state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_seq")
    private User user;
}


