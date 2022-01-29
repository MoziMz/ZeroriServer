package com.mozi.moziserver.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class ReqUserChallengeCreate {
    @NotNull
    private Long challengeSeq;

    @NotNull
    private LocalDate startDate;

    @NotNull List<ReqUserChallengePlanDate> planDates = new LinkedList<>();


//    @NotBlank
//    private String name;
//    @NotBlank
//    private String location;
//    @NotNull
//    private String operatingTime;
//    @NotNull
//    private String menu;
//    private List<ReqLink> links = new LinkedList<>();

}
