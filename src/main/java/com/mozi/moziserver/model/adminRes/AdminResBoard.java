package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResBoard {
    private final Long seq;
    private final String title;
    private final String content;

    private AdminResBoard(Board board) {
        this.seq = board.getSeq();
        this.title = board.getTitle();
        this.content = board.getContent();
    }

    public static AdminResBoard of(Board board) {
        return new AdminResBoard(board);
    }
}
