package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.DeclarationType;
import com.mozi.moziserver.model.req.*;
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

    @ApiOperation("스토리 생성")
    @PostMapping("/v1/challenges/{seq}/confirms")
    public ResponseEntity<Void> createConfirm(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq,
            @RequestBody @Valid ReqConfirmCreate reqConfirmCreate
    ){
        confirmService.createConfirm(userSeq, seq,reqConfirmCreate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    //03.01
//    @ApiOperation("스토리 생성")
//    @PostMapping("/v1/challenges/{seq}/confirms")
//    public ResponseEntity<Void> createConfirm(
//            @ApiParam(hidden = true) @SessionUser Long userSeq,
//            @PathVariable Long seq,
//            @RequestBody @Valid ReqConfirmCreate reqConfirmCreate
//            ){
//        confirmService.createConfirm(userSeq, seq,reqConfirmCreate);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

//    @ApiOperation("인증 전체 조회")
//    @GetMapping("/v1/challenges/confirms")
//    public List<ResConfirmList> getAllConfirmList() {
//
//        return confirmService.getAllConfirmList()
//                .stream()
//                .map(ResConfirmList::of)
//                .collect(Collectors.toList());
//    }

    //0301
    @ApiOperation("스토리 전체 조회")
    @GetMapping("/v1/challenges/confirms")
    public List<ResConfirmList> getAllConfirmList(
            @Valid ReqConfirmList req
    ) {

        return confirmService.getAll(req)
                .stream()
                .map(ResConfirmList::of)
                .collect(Collectors.toList());
    }

    //0301
    @ApiOperation("챌린지별 스토리 전체 조회")
    @GetMapping("/v1/challenges/{seq}/confirms")
    public List<ResConfirmList> getConfirmList(
            @PathVariable Long seq,
            @Valid ReqConfirmList req
    ) {

        return confirmService.getConfirmList(seq,req)
                .stream()
                .map(ResConfirmList::of)
                .collect(Collectors.toList());
    }

    //0301
    //최신순
    @ApiOperation("본인 스토리 전체 조회")
    @GetMapping("/v1/users/confirms")
    public List<ResUserConfirmList> getUserConfirmList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqConfirmList req
    ) {

        return confirmService.getUserConfirmList(userSeq,req)
                .stream()
                .map(ResUserConfirmList::of)
                .collect(Collectors.toList());
    }

//    //본인 스토리 하나 조회
//    @ApiOperation("본인 인증 하나 조회")
//    @GetMapping("/v1/challenges/{seq}/confirms/{date}")
//    public ResConfirm getMyConfirm(
//            @ApiParam(hidden = true) @SessionUser Long userSeq,
//            @PathVariable("seq") Long seq,
//            @PathVariable("date") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date date
//    ) {
//        Confirm confirm=confirmService.getConfirm(userSeq,seq,date);
//
//        List<ConfirmSticker> confirmStickerList=confirmService.getConfirmStickerList(userSeq,seq,date);
//
//        return ResConfirm.of(confirm,confirmStickerList);
//    }

    //0301
    //본인 스토리 하나 조회->/v1/users/confirms/{seq}로 고치기
    @ApiOperation("본인 스토리 하나 조회")
    @GetMapping("/v1/users/confirms/{confirmSeq}")
    public ResConfirm getMyConfirm(
            @ApiParam(hidden = true) @SessionUser Long mySeq,
            @PathVariable Long confirmSeq
    ) {
        Confirm confirm=confirmService.getConfirm(mySeq,confirmSeq);

        List<ConfirmSticker> confirmStickerList=confirmService.getConfirmStickerList(
                confirmSeq
        );

        return ResConfirm.of(confirm,confirmStickerList);
    }

//    //0301
//    //참여자 스토리 하나 조회->challenges/{seq}/confirms/{confirm_seq}
//    @ApiOperation("참여자 스토리 하나 조회")
//    @GetMapping("/v1/challenges/user-confirms")
//    public ResConfirm getUserConfirm(
//            @Valid ReqConfirm req
//    ) {
//        Confirm confirm=confirmService.getConfirm(req.getUserSeq(),req.getChallengeSeq(),req.getDate());
//
//        List<ConfirmSticker> confirmStickerList=confirmService.getConfirmStickerList(
//                req.getUserSeq(),req.getChallengeSeq(),req.getDate()
//        );
//
//        return ResConfirm.of(confirm,confirmStickerList);
//    }

//    @ApiOperation("인증 삭제")
//    @DeleteMapping("/v1/challenges/{seq}/confirms/{date}")
//    public ResponseEntity<Void> deleteConfirm(
//            @ApiParam(hidden = true) @SessionUser Long userSeq,
//            @PathVariable("seq") Long seq,
//            @PathVariable("date") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date date
//    ){
//        confirmService.deleteConfirm(userSeq,seq,date);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

//    @ApiOperation("스토리 삭제")
//    @DeleteMapping("/v1/challenges/my-confirms")
//    public ResponseEntity<Void> deleteConfirm(
//            @Valid ReqConfirm req,
//            @ApiParam(hidden = true) @SessionUser Long mySeq
//    ){
//        confirmService.deleteConfirm(mySeq,req.getChallengeSeq(),req.getDate());
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @ApiOperation("신고 생성")
//    @PostMapping("/v1/confirms/declarations")
//    public ResponseEntity<Void> createDeclaration(
//            @ApiParam(hidden = true) @SessionUser Long userSeq,
//            @RequestBody @Valid ReqDeclarationCreate reqDeclarationCreate
//    ){
//        confirmService.createDeclaration(userSeq,reqDeclarationCreate);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @ApiOperation("스티커 전체 조회")
//    @GetMapping("/v1/confirms/stickers")
//    public List<ResStickerList> getStickerList(
//            @ApiParam(hidden = true) @SessionUser Long userSeq
//    ) {
//        List<UserStickerImg> userStickerImgList=confirmService.getUserStickerImg(userSeq);
//
//        return userStickerImgList
//                .stream()
//                .map(ResStickerList::of)
//                .collect(Collectors.toList());
//    }
//
//    @ApiOperation("유저 스티커 생성(다운로드)")
//    @PostMapping("/v1/confirms/stickers")
//    public ResponseEntity<Void> createUserSticker(
//            @ApiParam(hidden = true) @SessionUser Long userSeq,
//            @RequestBody @Valid ReqUserStickerList userStickerList
//    ){
//        confirmService.createUserSticker(userSeq, userStickerList);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

//    //자기 자신도 스티커 붙이는게 가능한가?
//    ///v1/confirms/confirm-stickers
//    ///v1/challenges/{seq}/confirms/{confirm_seq}/confirm-stickers
//    @ApiOperation("스티커 생성(부착)")
//    @PostMapping("/v1/challenges/{seq}/confirms/{confirm_seq}/confirm-stickers")
//    public ResponseEntity<Void> createConfirmSticker(
//            @RequestBody @Valid ReqConfirmSticker confirmSticker,
//            @PathVariable Long seq,
//            @PathVariable Long confirm_seq,
//            @ApiParam(hidden = true) @SessionUser Long mySeq
//    ){
//        confirmService.createConfirmSticker(mySeq,seq,confirm_seq, confirmSticker);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

}