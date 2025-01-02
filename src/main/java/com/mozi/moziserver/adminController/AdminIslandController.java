package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminIslandService;
import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.adminRes.AdminResIsland;
import com.mozi.moziserver.model.entity.DetailIsland;
import com.mozi.moziserver.model.entity.Island;
import io.swagger.v3.oas.annotations.Operation;
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

    // -------------------- -------------------- Island -------------------- -------------------- //
    @Operation(summary = "섬 하나 조회")
    @GetMapping(value = "/admin/islands/{seq}")
    public AdminResIsland getIsland(
            @PathVariable(value = "seq") Long seq
    ) {

        Island island = adminIslandService.getIsland(seq);
        List<DetailIsland> detailIslandList = adminIslandService.getDetailIslandListByIsland(island);

        return AdminResIsland.of(island, detailIslandList);
    }

    @Operation(summary = "섬 모두 조회")
    @GetMapping(value = "/admin/islands")
    public List<Island> getIslandList(
    ) {

        return adminIslandService.getIslandList();
    }

    @Operation(summary = "섬 정보(Island) 생성")
    @PostMapping(value = "/admin/islands")
    public ResponseEntity<Object> createIsland(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "description", required = true) String description,
            @RequestParam(value = "openRequiredPoint", required = true) Integer openRequiredPoint
    ) {

        adminIslandService.createIsland(name, description, openRequiredPoint);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "섬 정보(Island) 수정")
    @PutMapping(value = "/admin/islands/{seq}")
    public ResponseEntity<Object> updateIsland(
            @PathVariable(value = "seq", required = true) Long seq,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "openRequiredPoint", required = false) Integer openRequiredPoint
    ) {

        if (name != null || description != null || openRequiredPoint != null) {
            adminIslandService.updateIsland(seq, name, description, openRequiredPoint);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "섬 삭제")
    @DeleteMapping("/admin/islands/{seq}")
    public ResponseEntity<Object> deleteIsland(
            @PathVariable(value = "seq") Long seq
    ) {

        adminIslandService.deleteIsland(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- DetailIsland -------------------- -------------------- //

    @Operation(summary = "이미지(DetailIsland) 생성")
    @PostMapping(value = "/admin/detail-islands", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createIsland(
            @RequestParam("islandSeq") Long islandSeq,
            @RequestPart(value = "islandImgUrlList", required = true) List<MultipartFile> islandImgUrlList,
            @RequestPart(value = "islandThumbnailImgUrlList", required = true) List<MultipartFile> islandThumbnailImgUrlList
    ) {

        if (islandImgUrlList.size() != Constant.totalImagesPerIsland ||
                islandThumbnailImgUrlList.size() != Constant.totalImagesPerIsland) {
            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException("need to 11 images");
        }

        if (islandSeq != null) {
            adminIslandService.createDetailIsland(islandSeq, islandImgUrlList, islandThumbnailImgUrlList);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "섬 이미지(detailIsland) 수정")
    @PutMapping(value = "/admin/detail-islands/{seq}")
    public ResponseEntity<Object> updateIslandImg(
            @PathVariable("seq") Long seq,
            @RequestPart(value = "img", required = false) MultipartFile islandImg,
            @RequestPart(value = "islandThumbnailImg", required = false) MultipartFile islandThumbnailImg
    ) {

        if (islandImg != null || islandThumbnailImg != null) {
            adminIslandService.updateImgOfDetailIsland(seq, islandImg, islandThumbnailImg);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "섬 이미지(detailIsland) 삭제")
    @DeleteMapping("/admin/detail-islands/{seq}")
    public ResponseEntity<Object> deleteDetailIsland(
            @PathVariable(value = "seq") Long seq
    ) {

        adminIslandService.deleteDetailIsland(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
