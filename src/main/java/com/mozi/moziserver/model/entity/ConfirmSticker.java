package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "confirm_sticker")
public class ConfirmSticker extends AbstractTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name="confirm_seq")
    private Confirm confirm;

    @ManyToOne
    @JoinColumn(name="user_seq")
    User user;

    @ManyToOne
    @JoinColumn(name = "sticker_seq")
    StickerImg stickerImg;

    @Column(name = "location_x")
    private BigDecimal locationX;

    @Column(name = "location_y")
    private BigDecimal locationY;

    @Column(name = "angle")
    private BigDecimal angle;

    @Column(name = "inch")
    private BigDecimal inch;
}
