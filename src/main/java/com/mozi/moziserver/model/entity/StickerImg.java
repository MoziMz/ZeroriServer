package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sticker_img")
public class StickerImg extends AbstractTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name="img_url")
    private String imgUrl;

    @OneToMany
    @JoinColumn(name="seq")
    private List<UserSticker> userStickerList;

    @OneToMany
    @JoinColumn(name="seq")
    private List<ConfirmSticker> confirmStickerList;
}
