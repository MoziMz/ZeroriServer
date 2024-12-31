package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminBoardService;
import com.mozi.moziserver.model.adminReq.ReqAdminPostboxMessageAdminCreate;
import com.mozi.moziserver.model.adminRes.AdminResBoard;
import com.mozi.moziserver.model.adminRes.AdminResPostboxMessageAdmin;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminBoardController {

    private final AdminBoardService adminBoardService;

    @Operation(summary = "공지사항 리스트 조회")
    @GetMapping("/admin/boards")
    public List<AdminResBoard> getBoardList(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize
    ) {
        return adminBoardService.getBoardList(pageNumber, pageSize)
                .stream()
                .map(board -> AdminResBoard.of(board))
                .collect(Collectors.toList());
    }

    @Operation(summary = "공지사항 하나 조회")
    @GetMapping("/admin/boards/{seq}")
    public AdminResBoard getBoard(
            @PathVariable("seq") Long seq
    ) {
        return AdminResBoard.of(adminBoardService.getBoard(seq));
    }

    @Operation(summary = "공지사항 생성")
    @PostMapping("/admin/boards")
    public ResponseEntity<Object> createBoard(
            @RequestParam("title") String title,
            @RequestParam("content") String content
    ) {
        if (title != null && content != null) {
            adminBoardService.createBoard(title, content);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "공지사항 수정")
    @PutMapping("/admin/boards/{seq}")
    public ResponseEntity<Object> updateBoard(
            @PathVariable(value = "seq") Long seq,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content
    ) {
        if (title != null || content != null) {
            adminBoardService.updateBoard(seq, title, content);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "공지사항 삭제")
    @DeleteMapping("/admin/boards/{seq}")
    public ResponseEntity<Object> deleteBoard(
            @PathVariable("seq") Long seq
    ) {
        adminBoardService.deleteBoard(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "관리자 편지 리스트 조회")
    @GetMapping("/admin/postbox-message-admins")
    public List<AdminResPostboxMessageAdmin> getPostboxMessageAdminList(
            @RequestParam(value = "keyword", required = false) String keyword, //keyword is userSeq or content
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize
    ) {
        return adminBoardService.getPostBoxMessageAdminList(keyword, pageNumber, pageSize)
                .stream()
                .map(postboxMessageAdmin -> AdminResPostboxMessageAdmin.of(postboxMessageAdmin))
                .collect(Collectors.toList());
    }

    @Operation(summary = "관리자 편지 하나 조회")
    @GetMapping("/admin/postbox-message-admins/{seq}")
    public AdminResPostboxMessageAdmin getPostboxMessageAdmin(
            @PathVariable("seq") Long seq
    ) {
        return AdminResPostboxMessageAdmin.of(adminBoardService.getPostboxMessageAdmin(seq));
    }

    @Operation(summary = "관리자 편지 생성")
    @PostMapping("/admin/postbox-message-admins")
    public ResponseEntity<Object> createPostboxMessageAdmin(
            @RequestBody @Valid ReqAdminPostboxMessageAdminCreate req
    ) {
        adminBoardService.createPostboxMessageAdmin(req.getUserSeq(), req.getContent(), req.getSender());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "관리자 편지 수정")
    @PutMapping("/admin/postbox-message-admins/{seq}")
    public ResponseEntity<Object> updatePostboxMessageAdmin(
            @PathVariable(value = "seq") Long seq,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "sender", required = false) String sender,
            @RequestParam(value = "checked_state", required = false) Boolean checkedState
    ) {
        if (content != null || sender != null || checkedState != null) {
            adminBoardService.updatePostboxMessageAdmin(seq, content, sender, checkedState);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "관리자 편지 삭제")
    @DeleteMapping("/admin/postbox-message-admins/{seq}")
    public ResponseEntity<Object> deletePostboxMessageAdmin(
            @PathVariable("seq") Long seq
    ) {
        adminBoardService.deletePostboxMessageAdmin(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

