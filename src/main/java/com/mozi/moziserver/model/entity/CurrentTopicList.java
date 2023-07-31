package com.mozi.moziserver.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "current_topic_list")
public class CurrentTopicList extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer turn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_seq")
    private Topic topic;
}