package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;
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

    private BigDecimal locationX;

    private BigDecimal locationY;

    private BigDecimal angle;

    private BigDecimal inch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirm_seq")
    private Confirm confirm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sticker_seq")
    Sticker sticker;
}
