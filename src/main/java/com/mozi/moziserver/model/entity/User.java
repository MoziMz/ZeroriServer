package com.mozi.moziserver.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "user")
public class User extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String nickName;

    private String email;

    @OneToMany
    @JoinColumn(name="user_seq")
    private List<Confirm> confirmList;

    @OneToMany
    @JoinColumn(name="user_seq")
    private List<ConfirmSticker> confirmStickerList;

}
