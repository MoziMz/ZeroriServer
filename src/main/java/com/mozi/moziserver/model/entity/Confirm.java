package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "confirm")
public class Confirm extends AbstractTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name="user_seq")
    User user;

    @ManyToOne
    @JoinColumn(name = "challenge_seq")
    Challenge challenge;

    @Column(name = "date")
    private LocalDate date;

    @Column(name="img_url")
    private String imgUrl;

    @Column(name="declaration_state")
    private Byte confirmState;

    @OneToMany
    @JoinColumn(name="confirm_seq")
    private List<ConfirmSticker> confirmStickerList;

    @OneToMany
    @JoinColumn(name="confirm_seq")
    private List<Declaration> DeclarationList;

}
