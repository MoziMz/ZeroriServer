package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.ConfirmStateType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Entity(name = "confirm")
public class Confirm extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "img_url")
    private String imgUrl;

    @Builder.Default
    private Integer likeCnt = 0;

    @Builder.Default
    private Integer reportedCnt = 0;

    @Transient
    boolean isLiked;

    @Transient
    boolean isReported;

    @Enumerated(EnumType.STRING)
    private ConfirmStateType state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_seq")
    Challenge challenge;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirm_seq")
    private List<ConfirmSticker> confirmStickerList;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirm_seq")
    private List<ConfirmReport> confirmReportList;
}
