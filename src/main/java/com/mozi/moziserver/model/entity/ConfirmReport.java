package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.ConfirmReportType;
import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "confirm_report")
public class ConfirmReport extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ConfirmReportType confirmReportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirm_seq")
    private Confirm confirm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
}
