package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Board;
import com.mozi.moziserver.model.entity.QBoard;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class BoardRepositoryImpl extends QuerydslRepositorySupport implements BoardRepositorySupport {
    private final QBoard qBoard = QBoard.board;

    public BoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public List<Board> findAllByOrderByCreatedAt(
            Long userSeq,
            Integer pageSize,
            Long prevLastPostSeq
    ) {
        return from(qBoard)
                .orderBy(qBoard.createdAt.desc())
                .fetch();
    }
}
