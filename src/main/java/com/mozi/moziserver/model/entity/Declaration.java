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

    @EmbeddedId
    private ConfirmId id;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private DeclarationType declarationType;

}
