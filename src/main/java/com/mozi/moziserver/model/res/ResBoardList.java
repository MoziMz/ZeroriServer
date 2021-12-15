package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Board;
import lombok.Getter;
import java.time.format.DateTimeFormatter;


@Getter
public class ResBoardList {
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;

    private ResBoardList(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd"));
        this.updatedAt = board.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd"));
    }

    public static ResBoardList of(Board board) {
        return new ResBoardList(board);
    }
}
