package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "confirm")
//@Table(indexes = @Index(name="idx_challenge_seq", columnList = "challenge_seq, date"))
public class Confirm extends AbstractTimeEntity{

    @EmbeddedId
    private ConfirmId id;

    @Column(name="img_url")
    private String imgUrl;

}
