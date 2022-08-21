package com.mozi.moziserver.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "current_tag_list")
public class CurrentTagList extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer turn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_seq")
    private Tag tag;
}
