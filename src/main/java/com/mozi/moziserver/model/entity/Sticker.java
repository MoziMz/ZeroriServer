package com.mozi.moziserver.model.entity;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sticker")
public class Sticker extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "img_url")
    private String imgUrl;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "seq")
    private List<ConfirmSticker> confirmStickerList;
}
