package com.mozi.moziserver.adminService;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Board;
import com.mozi.moziserver.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminBoardService {
    private final BoardRepository boardRepository;

    public List<Board> getBoardList(Integer pageNumber, Integer pageSize) {
        return boardRepository.findAllByPaging(pageNumber, pageSize);
    }

    public Board getBoard(Long seq) {
        Board board = boardRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.BOARD_NOT_EXISTS::getResponseException);
        return board;
    }

    public void createBoard(String title, String content) {
        Board board = Board.builder()
                .title(title)
                .content(content)
                .build();

        boardRepository.save(board);
    }

    public void updateBoard(Long seq, String title, String content) {
        Board board = getBoard(seq);

        if (title != null) {
            board.setTitle(title);
        }

        if (content != null) {
            board.setContent(content);
        }

        boardRepository.save(board);
    }

    public void deleteBoard(Long seq) {
        Board board = getBoard(seq);
        boardRepository.delete(board);
    }
