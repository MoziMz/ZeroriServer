package com.mozi.moziserver.model.entity;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "postbox_message_admin")
public class PostboxMessageAdmin extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String sender;

    private String content;

    private boolean checkedState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
}
