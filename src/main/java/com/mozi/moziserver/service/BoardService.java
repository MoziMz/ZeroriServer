package com.mozi.moziserver.service;

import com.mozi.moziserver.model.entity.Board;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<Board> getAllBoardListByCreatedAt(Long userseq, ReqList req) {
        return boardRepository.findAllByOrderByCreatedAt(
                userseq,
                req.getPageSize(),
                req.getPrevLastSeq()
        );
    }
}
