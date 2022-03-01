package com.mozi.moziserver.model.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class ReqConfirm {

    private Long userSeq;

    @NotNull
    private Long challengeSeq;

    @NotNull
    private LocalDate date;
}
