package com.mozi.moziserver.adminService;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Board;
import com.mozi.moziserver.model.entity.PostboxMessageAdmin;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.repository.BoardRepository;
import com.mozi.moziserver.repository.PostboxMessageAdminRepository;
import com.mozi.moziserver.service.UserService;
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
    private final PostboxMessageAdminRepository postboxMessageAdminRepository;
    private final UserService userService;

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

    public PostboxMessageAdmin getPostboxMessageAdmin(Long seq) {
        PostboxMessageAdmin postboxMessageAdmin = postboxMessageAdminRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POSTBOX_MESSAGE_ADMIN_NOT_EXISTS::getResponseException);

        return postboxMessageAdmin;
    }

    public List<PostboxMessageAdmin> getPostBoxMessageAdminList(String keyword, Integer pageNumber, Integer pageSize) {

        Long numberOfKeyword = null;
        if (StringUtils.hasLength(keyword)) {
            try {
                numberOfKeyword = Long.valueOf(keyword);
            } catch (NumberFormatException e) {
            }
        }

        return postboxMessageAdminRepository.findAllByKeyword(keyword, numberOfKeyword, pageNumber, pageSize);
    }

    public void createPostboxMessageAdmin(Long userSeq, String content, String sender) {
        User user = userService.getUserBySeq(userSeq);

        PostboxMessageAdmin postboxMessageAdmin = PostboxMessageAdmin.builder()
                .user(user)
                .content(content)
                .sender(sender)
                .build();

        postboxMessageAdminRepository.save(postboxMessageAdmin);
    }

    public void updatePostboxMessageAdmin(Long seq, String content, String sender, Boolean checkedState) {
        PostboxMessageAdmin postboxMessageAdmin = getPostboxMessageAdmin(seq);

        if (content != null) {
            postboxMessageAdmin.setContent(content);
        }

        if (sender != null) {
            postboxMessageAdmin.setSender(sender);
        }

        if (checkedState != null) {
            postboxMessageAdmin.setCheckedState(checkedState);
        } else {
            postboxMessageAdmin.setCheckedState(false);
        }

        postboxMessageAdminRepository.save(postboxMessageAdmin);
    }

    public void deletePostboxMessageAdmin(Long seq) {
        PostboxMessageAdmin postboxMessageAdmin = getPostboxMessageAdmin(seq);

        postboxMessageAdminRepository.delete(postboxMessageAdmin);
    }

}
