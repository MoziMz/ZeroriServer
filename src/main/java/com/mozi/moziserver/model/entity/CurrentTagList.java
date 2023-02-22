package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
