package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositorySupport {
    List<Board> findAllByOrderByCreatedAt();
}
