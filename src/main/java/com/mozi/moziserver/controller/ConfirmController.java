package com.mozi.moziserver.controller;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.mappedenum.ConfirmListType;
import com.mozi.moziserver.model.req.ReqConfirmOfUser;
import com.mozi.moziserver.model.req.ReqConfirmSticker;
import com.mozi.moziserver.model.req.ReqDeclarationCreate;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.model.res.ResConfirmList;
import com.mozi.moziserver.model.res.ResStickerList;
import com.mozi.moziserver.model.res.ResWeekConfirm;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.ConfirmService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConfirmController {

    private final ConfirmService confirmService;

    @ApiOperation("스토리 생성")
    @PostMapping("/v1/challenges/{challengeSeq}/confirms")
    public ResponseEntity<Void> createConfirm(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long challengeSeq,
            @RequestPart MultipartFile image
    ) {
        if (image == null) {
            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException("need to images");
        }

        confirmService.createConfirm(userSeq, challengeSeq, image);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation("스토리 리스트 조회")
    @GetMapping("/v1/challenges/confirms")
    public List<ResConfirmList> getConfirmList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestParam(required = false) Optional<ConfirmListType> confirmListType,
            @Valid ReqList req
            )
    {
        if(confirmListType.isEmpty()){
            confirmListType=Optional.of(ConfirmListType.RECENT);
        }
        return confirmService.getConfirmList(userSeq, req, confirmListType.get())
                .stream()
                .map(ResConfirmList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("챌린지별 스토리 전체 조회")
    @GetMapping("/v1/challenges/{challengeSeq}/confirms")
    public List<ResConfirmList> getConfirmListByChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long challengeSeq,
            @Valid ReqList req
    ) {
        return confirmService.getConfirmListByChallenge(userSeq, challengeSeq, req)
                .stream()
                .map(ResConfirmList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("유저 챌린지별 스토리 전체 조회")
    @GetMapping("/v1/user-challenges/{userChallengeSeq}/confirms")
    public List<ResConfirmList> getConfirmListNyUserChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long userChallengeSeq
    ) {
        return confirmService.getConfirmListByUserChallenge(userSeq, userChallengeSeq)
                .stream()
                .map(ResConfirmList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("본인 스토리 전체 조회")
    @GetMapping("/v1/users/me/confirms")
    public List<ResConfirmList> getUserConfirmList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {
        return confirmService.getUserConfirmList(userSeq, req)
                .stream()
                .map(ResConfirmList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("스토리 삭제")
    @DeleteMapping("/v1/confirms/{seq}")
    public ResponseEntity<Void> deleteConfirm(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        confirmService.deleteConfirm(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("신고 생성")
    @PostMapping("/v1/confirms/{confirmSeq}/declarations")
    public ResponseEntity<Void> createDeclaration(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long confirmSeq,
            @RequestBody @Valid ReqDeclarationCreate req
    ) {
        confirmService.createDeclaration(userSeq, confirmSeq, req.getType());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("스티커 전체 조회")
    @GetMapping("/v1/stickers")
    public List<ResStickerList> getStickerList(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        return confirmService.getSticker()
                .stream()
                .map(ResStickerList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("스티커 생성(부착)")
    @PostMapping("/v1/confirms/{seq}/confirm-stickers")
    public ResponseEntity<Void> createConfirmSticker(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqConfirmSticker reqConfirmSticker,
            @PathVariable Long seq
    ) {
        confirmService.createConfirmSticker(userSeq, seq, reqConfirmSticker);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("스토리 좋아요")
    @PostMapping("/v1/confirms/{seq}/like")
    public ResponseEntity<Void> createConfirmLike(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        confirmService.createConfirmLike(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("스토리 좋아요 취소")
    @DeleteMapping ("/v1/confirms/{seq}/like")
    public ResponseEntity<Void> deleteConfirmLike(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        confirmService.deleteConfirmLike(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("제로리 주민들의 활동보기")
    @GetMapping("/v1/confirms/week")
    public ResWeekConfirm getWeekConfirm(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        return confirmService.getWeekConfirm();
    }

    @ApiOperation("챌린지별 상세 챌린지")
    @GetMapping("/v1/users/me/challenges/confirms")
    public List<ResConfirmList> getConfirmOfUser(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqConfirmOfUser reqConfirmOfUser
    ) {

        return confirmService.getConfirmListAboutPeriod(userSeq, reqConfirmOfUser)
                .stream()
                .map(ResConfirmList::of)
                .collect(Collectors.toList());
    }
}