package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.ChallengeThemeType;
import com.mozi.moziserver.model.mappedenum.DeclarationType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "declaration")
public class Declaration extends AbstractTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="confirm_seq")
    private Confirm confirm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_seq")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private DeclarationType declarationType;

}
