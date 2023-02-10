package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminConfirmService;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.adminRes.AdminResConfirmList;
import com.mozi.moziserver.model.mappedenum.ConfirmStateType;
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
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminConfirmController {

    private final AdminConfirmService adminConfirmService;

    @ApiOperation("인증 리스트 조회")
    @GetMapping("/admin/confirms")
    public List<AdminResConfirmList> getConfirmList(
            @RequestParam(name = "userSeq", required = false) Long userSeq,
            @RequestParam(name = "confirmState", required = false) List<ConfirmStateType> confirmSateTypeList,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize
    ) {
        return adminConfirmService.getConfirmList(userSeq, confirmSateTypeList, pageNumber, pageSize)
                .stream().map(AdminResConfirmList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("인증 상태 변경 (BLOCKED or ACTIVE 상태로)")
    @GetMapping("/admin/confirms/{seq}")
    public ResponseEntity<Object> updateConfirmState(
            @PathVariable("seq") Long seq,
            @RequestParam(name = "confirmState", required = false) ConfirmStateType confirmSateType
    ) {
        if (!(confirmSateType == ConfirmStateType.BLOCKED || confirmSateType == ConfirmStateType.ACTIVE)) {
            throw ResponseError.BadRequest.INVALID_CONFIRM_STATE_TYPE.getResponseException();
        }

        adminConfirmService.updateConfirmState(seq, confirmSateType);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("인증 삭제")
    @DeleteMapping("/admin/confirms/{seq}")
    public ResponseEntity<Object> deleteConfirm(
            @PathVariable("seq") Long seq
    ) {
        adminConfirmService.deleteConfirm(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
