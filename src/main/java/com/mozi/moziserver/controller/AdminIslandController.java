package com.mozi.moziserver.controller;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.IslandService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminIslandController {

    private final IslandService islandService;

    @ApiOperation("섬 등록")
    @PostMapping(value = "/admin/islands",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createIsland(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestParam("name") String name,
            @RequestParam("type") Integer type,
            @RequestParam("description") String description,
            @RequestParam("maxPoint") Integer maxPoint,
            @RequestParam("maxRewardLevel") Integer maxRewardLevel,
            @RequestPart(value = "islandImgUrlList",required = false) List<MultipartFile> islandImgUrlList,
            @RequestPart(value = "islandThumbnailImgUrlList",required = false) List<MultipartFile> islandThumbnailImgUrlList
    ) {
        if (islandImgUrlList.size() != Constant.islandMaxLevel ||
                islandThumbnailImgUrlList.size() != Constant.islandMaxLevel){
            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException("need to 6 images");
        }

        islandService.createIsland(name,type,description,maxPoint,maxRewardLevel,islandImgUrlList, islandThumbnailImgUrlList);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("섬 정보 수정")
    @PutMapping(value = "/admin/islands")
    public ResponseEntity<Object> updateIsland(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestParam("name") String name,
            @RequestParam("type") Integer type,
            @RequestParam("description") String description,
            @RequestParam("maxPoint") Integer maxPoint,
            @RequestParam("maxRewardLevel") Integer maxRewardLevel
    ) {
        if (name != null || type != null || description != null || maxPoint !=null || maxRewardLevel !=null) {
            islandService.updateIsland(name,type,description,maxPoint,maxRewardLevel);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @ApiOperation("섬 이미지 수정")
    @PutMapping(value = "/admin/islandImgs")
    public ResponseEntity<Object> updateIslandImg(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestParam(value = "type", required = true) Integer type,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestPart(value = "islandImg", required = false) MultipartFile islandImg,
            @RequestPart(value = "islandThumbnailImg", required = false) MultipartFile islandThumbnailImg
    ) {
        if (islandImg != null || islandThumbnailImg != null) {
            islandService.updateIslandImg(type, level, islandImg, islandThumbnailImg);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
