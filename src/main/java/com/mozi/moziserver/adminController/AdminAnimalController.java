package com.mozi.moziserver.adminController;

import com.mozi.moziserver.adminService.AdminAnimalService;
import com.mozi.moziserver.adminService.AdminIslandService;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.adminRes.AdminResAnimal;
import com.mozi.moziserver.model.adminRes.AdminResAnimalItem;
import com.mozi.moziserver.model.adminRes.AdminResAnimalList;
import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.AnimalItem;
import com.mozi.moziserver.model.entity.Island;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminAnimalController {

    private final AdminAnimalService adminAnimalService;
    private final AdminIslandService adminIslandService;

    // -------------------- -------------------- animal -------------------- -------------------- //
    @ApiOperation("동물 하나 조회")
    @GetMapping("/admin/animals/{seq}")
    public AdminResAnimal getAnimal(
            @PathVariable("seq") Long seq
    ) {

        Animal animal = adminAnimalService.getAnimal(seq);
        List<AnimalItem> animalItemList = adminAnimalService.getAnimalItemListByAnimal(animal);

        return AdminResAnimal.of(animal, animalItemList);
    }

    @ApiOperation("동물 리스트 조회")
    @GetMapping("/admin/animals")
    public List<AdminResAnimalList> getAnimalList(
            @RequestParam(name = "keyword", required = false) Long keyword, //islandSeq
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize
    ) {

        Island island = null;
        if (keyword != null) {
            island = adminIslandService.getIsland(keyword);
        }

        return adminAnimalService.getAnimalList(island, pageNumber, pageSize)
                .stream().map(animal -> AdminResAnimalList.of(animal))
                .collect(Collectors.toList());
    }

    @ApiOperation("동물 생성")
    @PostMapping("/admin/animals")
    public ResponseEntity<Object> createAnimal(
            @RequestParam(name = "name") @NotBlank String name,
            @RequestParam(name = "turn") @NotBlank Integer turn,
            @RequestParam(name = "postboxAnimalContent") @NotBlank String postboxAnimalContent,
            @RequestPart MultipartFile image,
            @RequestPart MultipartFile fullBodyImage,
            @RequestParam(name = "islandSeq") @NotBlank Long islandSeq
    ) {

        if (image == null || fullBodyImage == null) {
            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException("need to images");
        }

        Island island = adminIslandService.getIsland(islandSeq);

        adminIslandService.checkLastIsland(island);

        adminAnimalService.createAnimal(name, turn, postboxAnimalContent, image, fullBodyImage, island);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("동물 수정")
    @PutMapping("/admin/animals/{seq}")
    public ResponseEntity<Object> updateAnimal(
            @PathVariable("seq") Long seq,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "postboxAnimalContent", required = false) String postboxAnimalContent,
            @RequestPart(required = false) MultipartFile image,
            @RequestPart(required = false) MultipartFile fullBodyImage,
            @RequestParam(name = "islandSeq", required = false) Long islandSeq
    ) {

        if (name != null || postboxAnimalContent != null || image != null || fullBodyImage != null || islandSeq != null) {
            Island island = islandSeq != null ? adminIslandService.getIsland(islandSeq) : null;
            adminAnimalService.updateAnimal(seq, name, postboxAnimalContent, image, fullBodyImage, island);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("동물 삭제")
    @DeleteMapping("/admin/animals/{seq}")
    public ResponseEntity<Object> deleteAnimal(
            @PathVariable("seq") Long seq
    ) {

        adminAnimalService.deleteAnimal(seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------- -------------------- AnimalItem -------------------- -------------------- //
    @ApiOperation("동물 아이템 하나 조회")
    @GetMapping("/admin/animal-items/{seq}")
    public AdminResAnimalItem getAnimalItem(
            @PathVariable("seq") Long seq
    ) {

        return AdminResAnimalItem.of(adminAnimalService.getAnimalItem(seq));
    }

    @ApiOperation("동물 아이템 리스트 조회")
    @GetMapping("/admin/animal-items")
    public List<AdminResAnimalItem> getAnimalItemList(
            @RequestParam(value = "animalSeq", required = false) Long animalSeq,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") @Max(30) Integer pageSize
    ) {

        return adminAnimalService.getAnimalItemListByPaging(animalSeq, pageNumber, pageSize)
                .stream().map(animalItem -> AdminResAnimalItem.of(animalItem))
                .collect(Collectors.toList());
    }

    @ApiOperation("동물의 아이템 등록")
    @PostMapping("/admin/animal-items")
    public ResponseEntity<Object> createPreparationItem(
            @RequestParam("animalSeq") Long animalSeq,
            @RequestParam("turn") @Min(1L) @Max(2L) Integer turn,
            @RequestParam("name") String name,
            @RequestPart("colorImage") MultipartFile colorImage,
            @RequestPart("blackImage") MultipartFile blackImage,
            @RequestParam("acquisitionRequiredPoint") Integer acquisitionRequiredPoint
    ) {

        if (colorImage == null || blackImage == null) {
            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException("need to images");
        }

        adminAnimalService.createAnimalItem(animalSeq, turn, name, colorImage, blackImage, acquisitionRequiredPoint);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("동물의 아이템 수정")
    @PutMapping("/admin/animal-items/{seq}")
    public ResponseEntity<Object> updatePreparationItem(
            @PathVariable(value = "seq") Long itemSeq,
            @RequestParam("animalSeq") Long animalSeq,
            @RequestParam("turn") @Min(1L) @Max(2L) Integer turn,
            @RequestParam(name = "name", required = false) String name,
            @RequestPart(name = "colorImage", required = false) MultipartFile colorImage,
            @RequestPart(name = "blackImage", required = false) MultipartFile blackImage,
            @RequestPart(name = "acquisitionRequiredPoint", required = false) Integer acquisitionRequiredPoint
    ) {

        if ((animalSeq != null && turn != null) && (name != null || colorImage != null || blackImage != null || acquisitionRequiredPoint != null)) {
            adminAnimalService.updateAnimalItem(itemSeq, animalSeq, turn, name, colorImage, blackImage, acquisitionRequiredPoint);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("동물의 아이템 삭제")
    @DeleteMapping("/admin/animal-items/{seq}")
    public ResponseEntity<Object> deleteAnimalItem(
            @PathVariable(value = "seq") Long itemSeq
    ) {

        adminAnimalService.deleteAnimalItem(itemSeq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}