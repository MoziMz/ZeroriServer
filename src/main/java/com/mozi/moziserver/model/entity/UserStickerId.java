package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
@EqualsAndHashCode(of = {"user","stickerImg"})
public class UserStickerId implements Serializable {
    @ManyToOne
    @JoinColumn(name="user_seq")
    User user;

    @ManyToOne
    @JoinColumn(name = "sticker_seq")
    StickerImg stickerImg;
}
