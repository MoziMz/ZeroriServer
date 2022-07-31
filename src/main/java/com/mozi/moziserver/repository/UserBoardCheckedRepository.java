package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.UserBoardChecked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface UserBoardCheckedRepository extends JpaRepository<UserBoardChecked, Long> {

    @Modifying
    @Query(name = "SELECT FROM user_board_checked WHERE user.seq = :userSeq AND board.seq IN boardSeqList")
    List<UserBoardChecked> findAllByUserSeqAndBoardSeqIn(
            @Param("userSeq") Long userSeq,
            @Param("boardSeqList") List<Long> boardSeqList);
}
