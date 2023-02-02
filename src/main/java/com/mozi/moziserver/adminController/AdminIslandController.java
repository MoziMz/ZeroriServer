package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminIslandService;
import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.adminRes.AdminResIsland;
import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.model.entity.IslandImg;
import com.mozi.moziserver.security.SessionUser;
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

    private final AdminIslandService adminIslandService;

    @ApiOperation("섬 리스트 조회")
    @GetMapping(value = "/admin/islands")
    public List<Island> getIsland(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ) {
        return adminIslandService.getIslandList();
    }

    @ApiOperation("섬 하나 조회")
    @GetMapping(value = "/admin/islands/{type}")
    public AdminResIsland getIsland(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable(value = "type", required = true) Integer type
    ) {
        Island island = adminIslandService.getIsland(type);
        List<IslandImg> islandImgList = adminIslandService.getIslandImgListByType(type);

        return AdminResIsland.of(island, islandImgList);
    }

    @ApiOperation("섬 등록")
    @PostMapping(value = "/admin/islands", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createIsland(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "type", required = true) Integer type,
            @RequestParam(value = "description", required = true) String description,
            @RequestParam(value = "maxPoint", required = true) Integer maxPoint,
            @RequestParam(value = "maxRewardLevel", required = true) Integer maxRewardLevel,
            @RequestPart(value = "islandImgUrlList", required = true) List<MultipartFile> islandImgUrlList,
            @RequestPart(value = "islandThumbnailImgUrlList", required = true) List<MultipartFile> islandThumbnailImgUrlList
    ) {
        if (islandImgUrlList.size() != Constant.islandMaxLevel ||
                islandThumbnailImgUrlList.size() != Constant.islandMaxLevel) {
            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException("need to 6 images");
        }

        adminIslandService.createIsland(type, name, description, maxPoint, maxRewardLevel, islandImgUrlList, islandThumbnailImgUrlList);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("섬 정보 수정")
    @PutMapping(value = "/admin/islands/{type}")
    public ResponseEntity<Object> updateIsland(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable(value = "type", required = true) Integer type,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "maxPoint", required = false) Integer maxPoint,
            @RequestParam(value = "maxRewardLevel", required = false) Integer maxRewardLevel
    ) {
        if (name != null || description != null || maxPoint != null || maxRewardLevel != null) {
            adminIslandService.updateIsland(type, name, description, maxPoint, maxRewardLevel);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("섬 이미지 수정")
    @PutMapping(value = "/admin/islands/{type}/island-imgs/{level}")
    public ResponseEntity<Object> updateIslandImg(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable(value = "type", required = true) Integer type,
            @PathVariable(value = "level", required = true) Integer level,
            @RequestPart(value = "islandImg", required = false) MultipartFile islandImg,
            @RequestPart(value = "islandThumbnailImg", required = false) MultipartFile islandThumbnailImg
    ) {
        if (islandImg != null || islandThumbnailImg != null) {
            adminIslandService.updateIslandImg(type, level, islandImg, islandThumbnailImg);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("섬 삭제")
    @DeleteMapping("/admin/islands/{type}")
    public ResponseEntity<Object> deleteIsland(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable(value = "type", required = true) Integer type
    ) {
        adminIslandService.deleteIsland(type);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
