package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminAnimalService;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.AnimalService;
import com.mozi.moziserver.service.PostboxMessageAnimalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.websocket.server.PathParam;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminAnimalController {
//
//    private final AdminAnimalService adminAnimalService;
//
//    // TODO 동물의 아이템까지 정보를 어떻게 제시할지 논의 필요
//    @ApiOperation("동물 리스트 조회")
//    @GetMapping("/admin/animals")
//    public List<Animal> getAnimalList(
//            @ApiParam(hidden = true) @SessionUser Long userSeq
//    ) {
//        return adminAnimalService.getAnimalList();
//    }
//
//    @ApiOperation("동물 생성")
//    @PostMapping("/admin/animals")
//    public ResponseEntity<Object> createAnimal(
//            @ApiParam(hidden = true) @SessionUser Long userSeq,
//            @RequestParam(name = "name") @NotBlank  String name,
//            @RequestParam(name = "postboxAnimalContent") @NotBlank String postboxAnimalContent,
//            @RequestParam(name = "islandType") @NotBlank Integer islandType,
//            @RequestParam(name = "islandLevel") @NotBlank Integer islandLevel,
//            @RequestPart MultipartFile image,
//            @RequestPart MultipartFile fullBodyImage
//
//    ) {
//        if (image == null || fullBodyImage == null) {
//            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException("need to images");
//        }
//
//        adminAnimalService.createAnimal(name, postboxAnimalContent, image, fullBodyImage, islandType, islandLevel);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @ApiOperation("동물 수정")
//    @PutMapping("/admin/animals/{seq}")
//    public ResponseEntity<Object> updateAnimal(
//            @ApiParam(hidden = true) @SessionUser Long userSeq,
//            @PathVariable("seq") Long seq,
//            @RequestParam(name = "name", required = false) String name,
//            @RequestParam(name = "postboxAnimalContent", required = false) String postboxAnimalContent,
//            @RequestPart(required = false) MultipartFile image,
//            @RequestPart(required = false) MultipartFile fullBodyImage
//    ) {
//
//        if (name != null || postboxAnimalContent != null || image != null || fullBodyImage != null) {
//            adminAnimalService.updateAnimal(seq, name, postboxAnimalContent, image, fullBodyImage);
//        }
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    // TODO 동물의 아이템 등록 URI /admin/animals/{seq}/items
//    @ApiOperation("동물의 준비물 아이템 등록")
//    @PostMapping("/admin/animals/{seq}/preparation-items")
//    public ResponseEntity<Object> createPreparationItem(
//            @ApiParam(hidden = true) @SessionUser Long userSeq,
//            @PathVariable("seq") Long animalSeq,
//            @RequestParam("turn") @Min(1L) @Max(2L) Integer turn,
//            @RequestParam("name") String name,
//            @RequestPart("colorImage") MultipartFile colorImage,
//            @RequestPart("blackImage") MultipartFile blackImage
//    ) {
//        if (colorImage == null || blackImage == null) {
//            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException("need to images");
//        }
//
//        adminAnimalService.createPreparationItem(animalSeq, turn, name, colorImage, blackImage);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @ApiOperation("동물의 준비물 아이템 수정")
//    @PutMapping("/admin/animals/{seq}/preparation-items")
//    public ResponseEntity<Object> updatePreparationItem(
//            @ApiParam(hidden = true) @SessionUser Long userSeq,
//            @PathVariable("seq") Long animalSeq,
//            @RequestParam("turn") @Min(1L) @Max(2L) Integer turn,
//            @RequestParam(name="name",required = false) String name,
//            @RequestPart(name = "colorImage",required = false) MultipartFile colorImage,
//            @RequestPart(name = "blackImage",required = false) MultipartFile blackImage
//    ) {
//        if ((animalSeq != null && turn != null)&&( name != null || colorImage != null || blackImage != null)) {
//            adminAnimalService.updatePreparationItem(animalSeq, turn, name, colorImage, blackImage);
//        }
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}