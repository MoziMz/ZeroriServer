package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.PriorityType;
import com.mozi.moziserver.model.mappedenum.QuestionCategoryType;
import com.mozi.moziserver.model.mappedenum.QuestionStateType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity(name = "question")
@NoArgsConstructor
@AllArgsConstructor
public class Question extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String title;

    private String content;

    private String imgUrl;

    private String email;

    private QuestionCategoryType category;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private QuestionStateType state = QuestionStateType.NEW;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PriorityType priority = PriorityType.NONE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
}
