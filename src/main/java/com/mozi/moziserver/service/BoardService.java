package com.mozi.moziserver.service;

import com.mozi.moziserver.common.JpaUtil;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.repository.BoardRepository;
import com.mozi.moziserver.repository.UserBoardCheckedRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserBoardCheckedRepository userBoardCheckedRepository;
    private final UserRepository userRepository;

    public List<Board> getAllBoardListByCreatedAt(Long userSeq, ReqList req) {
        List<Board> boardList =  boardRepository.findAllByOrderByCreatedAt(
                req.getPageSize(),
                req.getPrevLastSeq()
        );

        return setBoardChecked(userSeq, boardList);
    }

    private List<Board> setBoardChecked(Long userSeq, List<Board> boardList) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.InternalServerError.UNEXPECTED_ERROR::getResponseException);

        List<UserBoardChecked> userBoardCheckedList = userBoardCheckedRepository.findAllByUserSeqAndBoardSeqIn(userSeq, boardList.stream().map(board -> board.getSeq()).collect(Collectors.toList()));
        HashSet<Long> checkedBoardSeqSet = new HashSet<>(userBoardCheckedList.stream().map(userBoardChecked -> userBoardChecked.getBoard().getSeq()).collect(Collectors.toList()));
        for (Board board : boardList) {
            boolean isChecked = checkedBoardSeqSet.contains(board.getSeq());
            board.setChecked(isChecked);
        }

        return boardList;
    }
    @Transactional
    public void checkBoard(Long userSeq, Long seq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Board board = boardRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.BOARD_NOT_EXISTS::getResponseException);

        UserBoardChecked userBoardChecked = UserBoardChecked.builder()
                .user(user)
                .board(board)
                .build();


        try {
            userBoardCheckedRepository.save(userBoardChecked);
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_CHECKED_BOARD.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }
}
