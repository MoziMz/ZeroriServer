package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.PreparationItem;
import com.mozi.moziserver.model.entity.UserNotice;
import com.mozi.moziserver.model.mappedenum.UserNoticeType;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.model.res.*;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.AnimalService;
import com.mozi.moziserver.service.PostboxMessageAdminService;
import com.mozi.moziserver.service.PostboxMessageAnimalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostboxMessageController {

    private final PostboxMessageAdminService postboxMessageAdminService;
    private final PostboxMessageAnimalService postboxMessageAnimalService;
    private final AnimalService animalService;

    @ApiOperation("관리자의 편지 리스트 조회")
    @GetMapping("/v1/postbox-message-admins")
    public List<ResPostboxMessageAdminList> getPostboxMessageAdminList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {
        return postboxMessageAdminService.getPostboxMessageAdminList(userSeq, req)
                .stream()
                .map(ResPostboxMessageAdminList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("동물의 편지 리스트 조회")
    @GetMapping("/v1/postbox-message-animals")
    public List<ResPostboxMessageAnimalList> getPostboxMessageAnimalList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {
        return postboxMessageAnimalService.getPostboxMessageAnimalList(userSeq, req)
                .stream()
                .map(ResPostboxMessageAnimalList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("동물의 편지 하나 조회")
    @GetMapping("/v1/postbox-message-animals/{seq}")
    public ResPostboxMessageAnimal getPostboxMessageAnimal(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq

    ) {
        PostboxMessageAnimal postboxMessageAnimal = postboxMessageAnimalService.getPostboxMessageAnimal(userSeq, seq);
        List<PreparationItem> preparationItemList = postboxMessageAnimalService.getItemList(userSeq, postboxMessageAnimal.getAnimal().getSeq());

        postboxMessageAnimalService.checkMessage(userSeq, postboxMessageAnimal.getSeq());

        return ResPostboxMessageAnimal.of(postboxMessageAnimal, preparationItemList);
    }

    @ApiOperation("관리자 편지 확인 완료")
    @PutMapping("/v1/postbox-message-admins/{seq}/checked")
    public ResponseEntity<Object> checkUserChallenge(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq
    ) {
        postboxMessageAdminService.checkMessage(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("동물 하나 조회")
    @GetMapping("v1/animals/{seq}")
    public ResAnimal getAnimal(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq
    ) {
        return ResAnimal.of(animalService.getAnimal(seq));
    }

    @ApiOperation("유저 알림 조회")
    @GetMapping("/v1/user-notices/{type}")
    public ResUserNotice getUserNotice(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable UserNoticeType type
    ) {
        //UserNotice에서 type: PostboxMessageAnimal이고 checked_state: false인 User 조회한다.
        UserNotice userNotice = postboxMessageAnimalService.getUserNoticeByUserAndType(userSeq,type);

        return ResUserNotice.of(userNotice);
    }

    @ApiOperation("유저 알림 확인")
    @PutMapping("/v1/user-notices/{type}/checked")
    public ResponseEntity<Object> checkUserNotice(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable UserNoticeType type
    ) {
        postboxMessageAnimalService.checkUserNotice(userSeq, type);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
