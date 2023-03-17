package com.mozi.moziserver.controller;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.User;
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
import com.mozi.moziserver.service.UserService;
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
    private final UserService userService;

    @ApiOperation("스토리 리스트 조회")
    @GetMapping("/v1/challenges/confirms")
    public List<ResConfirmList> getConfirmList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestParam(required = false) Optional<ConfirmListType> confirmListType,
            @Valid ReqList req
    )
    {

        User user = userService.getUserBySeq(userSeq);

        if(confirmListType.isEmpty()){
            confirmListType=Optional.of(ConfirmListType.ALL);
        }
        return confirmService.getConfirmList(user, req, confirmListType.get())
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

        User user = userService.getUserBySeq(userSeq);

        return confirmService.getConfirmListByChallenge(user, challengeSeq, req)
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

        User user = userService.getUserBySeq(userSeq);

        return confirmService.getConfirmListByUserChallenge(user, userChallengeSeq)
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

        User user = userService.getUserBySeq(userSeq);

        return confirmService.getUserConfirmList(user, req)
                .stream()
                .map(ResConfirmList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("제로리 주민들의 활동보기")
    @GetMapping("/v1/confirms/week")
    public ResWeekConfirm getWeekConfirm(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestParam(required = false) Optional<ConfirmListType> confirmListType

    ) {

        if(confirmListType.isEmpty()){
            confirmListType=Optional.of(ConfirmListType.ALL);
        }

        return confirmService.getWeekConfirm(confirmListType.get());
    }

    @ApiOperation("챌린지별 상세 스토리")
    @GetMapping("/v1/users/me/challenges/confirms")
    public List<ResConfirmList> getConfirmOfUser(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqConfirmOfUser reqConfirmOfUser
    ) {

        User user = userService.getUserBySeq(userSeq);

        return confirmService.getConfirmListAboutPeriod(user, reqConfirmOfUser)
                .stream()
                .map(ResConfirmList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("스토리 생성")
    @PostMapping("/v1/challenges/{challengeSeq}/confirms")
    public ResponseEntity<Object> createConfirm(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long challengeSeq,
            @RequestPart MultipartFile image
    ) {

        User user = userService.getUserBySeq(userSeq);

        if (image == null) {
            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException("need to images");
        }

        confirmService.createConfirm(user, challengeSeq, image);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("스토리 좋아요")
    @PostMapping("/v1/confirms/{seq}/like")
    public ResponseEntity<Object> createConfirmLike(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {

        User user = userService.getUserBySeq(userSeq);
        confirmService.createConfirmLike(user, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("스토리 좋아요 취소")
    @DeleteMapping ("/v1/confirms/{seq}/like")
    public ResponseEntity<Object> deleteConfirmLike(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {

        User user = userService.getUserBySeq(userSeq);
        confirmService.deleteConfirmLike(user, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("신고 생성")
    @PostMapping("/v1/confirms/{confirmSeq}/declarations")
    public ResponseEntity<Object> createDeclaration(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long confirmSeq,
            @RequestBody @Valid ReqDeclarationCreate req
    ) {

        User user = userService.getUserBySeq(userSeq);
        confirmService.createReport(user, confirmSeq, req.getType());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("스토리 삭제")
    @DeleteMapping("/v1/confirms/{seq}")
    public ResponseEntity<Object> deleteConfirm(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {

        User user = userService.getUserBySeq(userSeq);
        confirmService.deleteConfirm(user, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- ConfirmSticker -------------------- -------------------- //
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
    public ResponseEntity<Object> createConfirmSticker(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqConfirmSticker reqConfirmSticker,
            @PathVariable Long seq
    ) {

        User user = userService.getUserBySeq(userSeq);
        confirmService.createConfirmSticker(user, seq, reqConfirmSticker);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}