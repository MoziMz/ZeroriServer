package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.AnimalItem;
import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserNotice;
import com.mozi.moziserver.model.mappedenum.UserNoticeType;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.model.res.*;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
    private final UserService userService;
    private final UserRewardService userRewardService;

    // -------------------- -------------------- PostboxMessageAnimal -------------------- -------------------- //
    // TODO ERASE (NOT USED V2)
    @Operation(summary = "동물의 편지 하나 조회")
    @GetMapping("/v1/postbox-message-animals/{seq}")
    public ResPostboxMessageAnimal getPostboxMessageAnimalV1(
            @Parameter(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq

    ) {

        PostboxMessageAnimal postboxMessageAnimal = postboxMessageAnimalService.getPostboxMessageAnimal(userSeq, seq);
        List<AnimalItem> preparationItemList = animalService.getAnimalItemList(postboxMessageAnimal.getAnimal());
        postboxMessageAnimalService.checkMessage(postboxMessageAnimal.getSeq());

        return ResPostboxMessageAnimal.of(postboxMessageAnimal, preparationItemList);
    }

    @Operation(summary = "동물의 편지 리스트 조회")
    @GetMapping("/v1/postbox-message-animals")
    public List<ResPostboxMessageAnimalList> getPostboxMessageAnimalList(
            @Parameter(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {

        return postboxMessageAnimalService.getPostboxMessageAnimalList(userSeq, req)
                .stream()
                .map(ResPostboxMessageAnimalList::of)
                .collect(Collectors.toList());
    }

    // -------------------- -------------------- PostboxMessageAdmin -------------------- -------------------- //
    @Operation(summary = "관리자의 편지 리스트 조회")
    @GetMapping("/v1/postbox-message-admins")
    public List<ResPostboxMessageAdminList> getPostboxMessageAdminList(
            @Parameter(hidden = true) @SessionUser Long userSeq,
            @Valid ReqList req
    ) {

        User user = userService.getUserBySeq(userSeq);
        return postboxMessageAdminService.getPostboxMessageAdminList(user, req)
                .stream()
                .map(ResPostboxMessageAdminList::of)
                .collect(Collectors.toList());
    }

    @Operation(summary = "관리자 편지 확인 완료")
    @PutMapping("/v1/postbox-message-admins/{seq}/checked")
    public ResponseEntity<Object> checkUserChallenge(
            @Parameter(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq
    ) {

        postboxMessageAdminService.checkMessage(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- Animal -------------------- -------------------- //
    @Operation(summary = "동물 하나 조회")
    @GetMapping("/v1/animals/{seq}")
    public ResAnimal getAnimal(
            @Parameter(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq
    ) {

        return ResAnimal.of(animalService.getAnimal(seq));
    }

    // -------------------- -------------------- UserNotice -------------------- -------------------- //
    @Operation(summary = "유저 알림 조회")
    @GetMapping("/v1/user-notices/{type}")
    public ResUserNotice getUserNotice(
            @Parameter(hidden = true) @SessionUser Long userSeq,
            @PathVariable UserNoticeType type
    ) {

        UserNotice userNotice = postboxMessageAnimalService.getUserNoticeByUserAndType(userSeq, type);

        return ResUserNotice.of(userNotice);
    }

    @Operation(summary = "유저 알림 확인")
    @PutMapping("/v1/user-notices/{type}/checked")
    public ResponseEntity<Object> checkUserNotice(
            @Parameter(hidden = true) @SessionUser Long userSeq,
            @PathVariable UserNoticeType type
    ) {

        postboxMessageAnimalService.checkUserNotice(userSeq, type);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- v2 -------------------- //
    @Operation(summary = "동물의 편지 하나 조회")
    @GetMapping("/v2/postbox-message-animals/{seq}")
    public ResPostboxMessageAnimal getPostboxMessageAnimal(
            @Parameter(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq

    ) {

        PostboxMessageAnimal postboxMessageAnimal = postboxMessageAnimalService.getPostboxMessageAnimal(userSeq, seq);
        postboxMessageAnimalService.checkMessage(postboxMessageAnimal.getSeq());

        User user = userService.getUserBySeq(userSeq);

        Integer thisWeekUserRewardPoint = userRewardService.getUserPointOfThisWeek(user);

        return ResPostboxMessageAnimal.of(postboxMessageAnimal, thisWeekUserRewardPoint);
    }
}