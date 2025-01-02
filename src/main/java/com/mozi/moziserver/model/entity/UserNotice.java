package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.UserNoticeType;
import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_notice")
public class UserNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long relatedSeq; // 관련 컨텐츠의 seq

    private boolean checkedState;

    @Enumerated(EnumType.STRING)
    private UserNoticeType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
}
