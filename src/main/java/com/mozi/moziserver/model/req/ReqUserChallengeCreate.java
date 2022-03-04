package com.mozi.moziserver.model.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // TODO
    // 파라미터가 들어온 시간을 자동으로 받기 위해서
    @JsonIgnore
    LocalDate today = LocalDate.now();

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
