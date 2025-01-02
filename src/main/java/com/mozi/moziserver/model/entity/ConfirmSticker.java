package com.mozi.moziserver.model.entity;

import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "confirm_sticker")
public class ConfirmSticker extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "location_x")
    private BigDecimal locationX;

    @Column(name = "location_y")
    private BigDecimal locationY;

    @Column(name = "angle")
    private BigDecimal angle;

    @Column(name = "inch")
    private BigDecimal inch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="confirm_seq")
    private Confirm confirm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_seq")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sticker_seq")
    Sticker sticker;
}