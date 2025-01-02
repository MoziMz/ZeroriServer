package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.PointReasonType;
import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_point_record")
public class UserPointRecord extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer point;

    @Enumerated(EnumType.STRING)
    private PointReasonType reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
}