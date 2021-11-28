package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"tagName","challenge"})
public class ChallengeTagId implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(name="tag_name")
    private ChallengeTagType tagName;

    @ManyToOne
    @JoinColumn(name="challenge_seq")
    Challenge challenge;
}
