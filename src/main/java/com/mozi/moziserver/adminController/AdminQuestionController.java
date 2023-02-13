package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminQuestionService;
import com.mozi.moziserver.adminService.AdminSuggestionService;
import com.mozi.moziserver.model.adminRes.AdminResQuestionList;
import com.mozi.moziserver.model.adminRes.AdminResSuggestionList;
import com.mozi.moziserver.model.mappedenum.PriorityType;
import com.mozi.moziserver.model.mappedenum.QuestionCategoryType;
import com.mozi.moziserver.model.mappedenum.QuestionStateType;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AdminQuestionController {

    private final AdminQuestionService adminQuestionService;

    private final AdminSuggestionService adminSuggestionService;

    @ApiOperation("문의 리스트 조회")
    @GetMapping("/admin/questions")
    public List<AdminResQuestionList> getQuestionList(
            @RequestParam(name = "category", required = false) QuestionCategoryType category,
            @RequestParam(name = "state", required = false) QuestionStateType state,
            @RequestParam(name = "priority", required = false) PriorityType priority,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize
    ) {
        return adminQuestionService.getQuestionList(category, state, priority, pageNumber, pageSize)
                .stream().map(AdminResQuestionList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("문의 (상태, 우선순위) 변경")
    @PutMapping("/admin/questions/{seq}")
    public ResponseEntity<Object> updateQuestion(
            @PathVariable(value = "seq", required = true) Long seq,
            @RequestParam(name = "state", required = false) QuestionStateType state,
            @RequestParam(name = "priority", required = false) PriorityType priority
    ) {
        adminQuestionService.updateStateAndPriority(seq, state, priority);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("문의 삭제")
    @DeleteMapping("/admin/questions/{seq}")
    public ResponseEntity<Object> updateQuestion(
            @PathVariable(value = "seq", required = true) Long seq
    ) {
        adminQuestionService.deleteQuestion(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("챌린지 제안 리스트 조회")
    @GetMapping("/admin/suggestions")
    public List<AdminResSuggestionList> getSuggestionList(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize
    ) {
        return adminSuggestionService.getSuggestionList(pageNumber, pageSize)
                .stream().map(AdminResSuggestionList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("챌린지 제안 삭제")
    @DeleteMapping("/admin/suggestions/{seq}")
    public ResponseEntity<Object> deleteSuggestion(
            @PathVariable(value = "seq", required = true) Long seq
    ) {
        adminSuggestionService.deleteSuggestion(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
