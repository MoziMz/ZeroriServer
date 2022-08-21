package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tag")
public class Tag extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String name;
}
