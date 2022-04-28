package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import com.mozi.moziserver.model.entity.PreparationItem;
import com.mozi.moziserver.model.req.ReqBasic;
import com.mozi.moziserver.model.res.ResPostboxAdminList;
import com.mozi.moziserver.model.res.ResPostboxAnimalList;
import com.mozi.moziserver.model.res.ResPostboxMessageAnimalList;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.PostboxMessageAdminService;
import com.mozi.moziserver.service.PostboxMessageAnimalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("관리자의 편지 리스트 조회")
    @GetMapping("/v1/postbox-message-admins")
    public List<ResPostboxAdminList> getPostboxMessageAdminList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqBasic req
    ) {
        return postboxMessageAdminService.getPostboxMessageAdminList(userSeq, req)
                .stream()
                .map(ResPostboxAdminList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("동물의 편지 리스트 조회")
    @GetMapping("/v1/postbox-message-animals")
    public List<ResPostboxMessageAnimalList> getPostboxAnimalList(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        return postboxMessageAnimalService.getPostboxMessageAnimalList(userSeq)
                .stream()
                .map(ResPostboxMessageAnimalList::of)
                .collect(Collectors.toList());
    }

    // TODO 메서드명 변경
    @ApiOperation("동물 이미지와 준비물")
    @GetMapping("/v1/postbox-message-animals/{seq}")
    public ResPostboxAnimalList getAnimalAndItemsIfSuccess(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        PostboxMessageAnimal postboxMessageAnimal = postboxMessageAnimalService.getAnimalInfo(userSeq);
        List<PreparationItem> preparationItemList = postboxMessageAnimalService.getItemList(userSeq, postboxMessageAnimal.getAnimal().getSeq());

        return ResPostboxAnimalList.of(postboxMessageAnimal, preparationItemList);
    }
}
