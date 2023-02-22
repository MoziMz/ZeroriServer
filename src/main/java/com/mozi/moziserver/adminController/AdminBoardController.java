package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminBoardService;
import com.mozi.moziserver.model.adminRes.AdminResBoard;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminBoardController {

    private final AdminBoardService adminBoardService;

    @ApiOperation("공지사항 리스트 조회")
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

    @ApiOperation("공지사항 하나 조회")
    @GetMapping("/admin/boards/{seq}")
    public AdminResBoard getBoard(
            @PathVariable("seq") Long seq
    ) {
        return AdminResBoard.of(adminBoardService.getBoard(seq));
    }

    @ApiOperation("공지사항 생성")
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

    @ApiOperation("공지사항 수정")
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

    @ApiOperation("공지사항 삭제")
    @DeleteMapping("/admin/boards/{seq}")
    public ResponseEntity<Object> deleteBoard(
            @PathVariable("seq") Long seq
    ) {
        adminBoardService.deleteBoard(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
