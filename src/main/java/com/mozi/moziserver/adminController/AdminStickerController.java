package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminStickerService;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.StickerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminStickerController {

    private final AdminStickerService adminStickerService;

    @ApiOperation("스티커 생성")
    @PostMapping("/admin/stickers")
    public ResponseEntity<Object> createSticker(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestPart MultipartFile image
    ) {
        if (image == null) {
            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException("need to images");
        }

        adminStickerService.createSticker(image);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("스티커 수정")
    @PutMapping("/admin/stickers/{seq}")
    public ResponseEntity<Object> updateSticker(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable("seq") Long seq,
            @RequestPart MultipartFile image
    ) {
        if (image == null) {
            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException("need to images");
        }

        adminStickerService.updateSticker(seq, image);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
