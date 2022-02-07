package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.DeclarationType;
import com.mozi.moziserver.model.req.ReqConfirmCreate;
import com.mozi.moziserver.model.req.ReqDeclarationCreate;
import com.mozi.moziserver.model.req.ReqUserStickerList;
import com.mozi.moziserver.model.res.*;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.ConfirmService;
import com.mozi.moziserver.model.entity.UserStickerImg;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConfirmController {

    private final ConfirmService confirmService;

    @ApiOperation("인증 생성")
    @PostMapping("/v1/challenges/{seq}/confirms")
    public ResponseEntity<Void> createConfirm(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq,
            @RequestBody @Valid ReqConfirmCreate reqConfirmCreate
            ){
        confirmService.createConfirm(userSeq, seq,reqConfirmCreate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("인증 전체 조회")
    @GetMapping("/v1/challenges/confirms")
    public List<ResConfirmList> getAllConfirmList() {

        return confirmService.getAllConfirmList()
                .stream()
                .map(ResConfirmList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("챌린지별 인증 전체 조회")
    @GetMapping("/v1/challenges/{seq}/confirms")
    public List<ResConfirmList> getConfirmList(
            @PathVariable Long seq
    ) {

        return confirmService.getConfirmList(seq)
                .stream()
                .map(ResConfirmList::of)
                .collect(Collectors.toList());
    }

    //최신순
    @ApiOperation("마이페이지 인증 전체 조회")
    @GetMapping("/v1/me/confirms")
    public List<ResUserConfirmList> getUserConfirmList(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {

        return confirmService.getUserConfirmList(userSeq)
                .stream()
                .map(ResUserConfirmList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("인증 하나 조회")
    @GetMapping("/v1/challenges/{seq}/confirms/{date}")
    public ResConfirm getConfirm(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq,
            @PathVariable("date") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date date
    ) {
        Confirm confirm=confirmService.getConfirm(userSeq,seq,date);

        List<ConfirmSticker> confirmStickerList=confirmService.getConfirmStickerList(userSeq,seq,date);

        return ResConfirm.of(confirm,confirmStickerList);
    }

    @ApiOperation("인증 삭제")
    @DeleteMapping("/v1/challenges/{seq}/confirms/{date}")
    public ResponseEntity<Void> deleteConfirm(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq,
            @PathVariable("date") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date date
    ){
        confirmService.deleteConfirm(userSeq,seq,date);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("신고 생성")
    @PostMapping("/v1/confirms/declarations")
    public ResponseEntity<Void> createDeclaration(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqDeclarationCreate reqDeclarationCreate
    ){
        confirmService.createDeclaration(userSeq,reqDeclarationCreate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("스티커 전체 조회")
    @GetMapping("/v1/confirms/stickers")
    public List<ResStickerList> getStickerList(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        List<UserStickerImg> userStickerImgList=confirmService.getUserStickerImg(userSeq);

        return userStickerImgList
                .stream()
                .map(ResStickerList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("유저 스티커 생성")
    @PostMapping("/v1/confirms/stickers")
    public ResponseEntity<Void> createUserSticker(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqUserStickerList userStickerList
    ){
        confirmService.createUserSticker(userSeq, userStickerList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}