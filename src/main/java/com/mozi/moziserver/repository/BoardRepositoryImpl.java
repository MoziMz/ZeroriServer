package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Board;
import com.mozi.moziserver.model.entity.QBoard;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BoardRepositoryImpl extends QuerydslRepositorySupport implements BoardRepositorySupport {
    private final QBoard qBoard = QBoard.board;

    public BoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public List<Board> findAllByOrderByCreatedAt(
            Integer pageSize,
            Long prevLastPostSeq
    ) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qBoard.seq::lt,prevLastPostSeq),
        };

        return from(qBoard)
                .orderBy(qBoard.seq.desc())
                .where(predicates)
                .limit(pageSize)
                .fetch();
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}
