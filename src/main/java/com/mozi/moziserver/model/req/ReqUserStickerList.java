package com.mozi.moziserver.model.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReqUserStickerList {
    private List<Long> stickerSeqList;
}
