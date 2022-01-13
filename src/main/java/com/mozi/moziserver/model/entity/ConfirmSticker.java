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

//    private ConfirmId confirmId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_seq")
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "challenge_seq")
//    Challenge challenge;
//
//    @Column(name = "date")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date date;

    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "challenge_seq")
    private Long challengeSeq;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "sticker_seq")
    StickerImg stickerImg;

//    @Column(name = "sticker_seq")
//    private Long stickerSeq;

    @Column(name = "location_x")
    private BigDecimal locationX;

    @Column(name = "location_y")
    private BigDecimal locationY;

    @Column(name = "angle")
    private BigDecimal angle;

    @Column(name = "inch")
    private BigDecimal inch;
}
