package com.mozi.moziserver.model.req;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class ReqConfirmSticker {

    @NotNull
    private Long stickerSeq;

//    @NotNull
    private Long userSeq;

    @NotNull
    private BigDecimal locationX;

    @NotNull
    private BigDecimal locationY;

    @NotNull
    private BigDecimal angle;

    @NotNull
    private BigDecimal inch;
}
