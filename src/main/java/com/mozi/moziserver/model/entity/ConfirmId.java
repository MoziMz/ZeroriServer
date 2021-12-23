package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
@EqualsAndHashCode(of = {"user","challenge","date"})
public class ConfirmId implements Serializable {

    @ManyToOne
    @JoinColumn(name="user_seq")
    User user;

    @ManyToOne
    @JoinColumn(name = "challenge_seq")
    Challenge challenge;


    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

}
