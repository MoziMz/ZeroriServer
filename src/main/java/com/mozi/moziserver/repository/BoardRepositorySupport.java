package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Board;

import java.util.List;

public interface BoardRepositorySupport {
    List<Board> findAllByOrderByCreatedAt (
            Long userSeq,
            Integer pageSize,
            Long prevLastPostSeq
    );
}
