package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.PriorityType;
import com.mozi.moziserver.model.mappedenum.QuestionCategory;
import com.mozi.moziserver.model.mappedenum.QuestionStateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@Entity(name = "question")
@NoArgsConstructor
@AllArgsConstructor
public class Question extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    private QuestionCategory category;

    private String title;

    private String content;

    private String imgUrl;

    private String email;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private QuestionStateType state = QuestionStateType.NEW;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PriorityType priority = PriorityType.NONE;
}
