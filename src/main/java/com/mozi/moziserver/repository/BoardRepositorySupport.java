package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Board;
import com.mozi.moziserver.model.entity.UserReward;

import java.util.List;

public interface BoardRepositorySupport {
    List<Board> findAllByOrderByCreatedAt (
            Integer pageSize,
            Long prevLastPostSeq
    );

    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    List<Board> findAllByPaging(Integer pageNumber, Integer pageSize);
}
