package com.mozi.moziserver.adminService;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.common.JpaUtil;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.AnimalItem;
import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.repository.AnimalItemRepository;
import com.mozi.moziserver.repository.AnimalRepository;
import com.mozi.moziserver.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAnimalService {

    private final AdminIslandService adminIslandService;

    private final AnimalRepository animalRepository;
    private final AnimalItemRepository animalItemRepository;
    private final S3ImageService s3ImageService;

    // -------------------- -------------------- animal -------------------- -------------------- //
    public Animal getAnimal(Long seq) {

        Animal animal = animalRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.ANIMAL_NOT_EXISTS::getResponseException);

        return animal;
    }

    public List<Animal> getAnimalList(Island island, Integer pageNumber, Integer pageSize) {

        return animalRepository.findAllByIsland(island, pageNumber, pageSize);

    }

    public void createAnimal(String name, Integer turn, String postboxAnimalContent, MultipartFile image, MultipartFile fullBodyImage, Island island) {

        String imgUrl = null;
        try {
            imgUrl = s3ImageService.uploadFile(image, "animal");
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        String fullBodyImgUrl = null;
        try {
            fullBodyImgUrl = s3ImageService.uploadFile(fullBodyImage, "animal");
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        Animal animal = Animal.builder()
                .name(name)
                .postboxAnimalContent(postboxAnimalContent)
                .turn(turn)
                .imgUrl(imgUrl)
                .fullBodyImgUrl(fullBodyImgUrl)
                .island(island)
                .build();

        try {
            animalRepository.save(animal);
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_ANIMAL_NAME.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    public void updateAnimal(Long animalSeq, String name, String postboxAnimalContent, MultipartFile image, MultipartFile fullBodyImage, Island island) {

        final Animal animal = getAnimal(animalSeq);

        String imgUrl = null;
        if (image != null) {
            try {
                imgUrl = s3ImageService.uploadFile(image, "animal");
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            animal.setImgUrl(imgUrl);
        }

        String fullBodyImgUrl = null;
        if (fullBodyImage != null) {
            try {
                fullBodyImgUrl = s3ImageService.uploadFile(fullBodyImage, "animal");
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            animal.setFullBodyImgUrl(fullBodyImgUrl);
        }

        if (name != null && name.length() != 0 && animal.getName() != name) {
            animal.setName(name);
        }

        if (postboxAnimalContent != null && postboxAnimalContent.length() != 0) {
            animal.setPostboxAnimalContent(postboxAnimalContent);
        }

        if (island != null) {
            animal.setIsland(island);
        }

        try {
            animalRepository.save(animal);
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException("already created(name / islandSeq and turn");
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    @Transactional
    public void deleteAnimal(Long seq) {

        Animal animal = getAnimal(seq);

        adminIslandService.checkLastIsland(animal.getIsland());

        if (animalItemRepository.countByAnimal(animal) > 0) {
            throw ResponseError.BadRequest.INVALID_ANIMAL_SEQ.getResponseException("Animal items remain. Please delete the animal items first.");
        }

        animalRepository.delete(animal);
    }

    // -------------------- -------------------- animalItem -------------------- -------------------- //
    public AnimalItem getAnimalItem(Long seq) {

        AnimalItem animalItem = animalItemRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.ANIMAL_ITEM_NOT_EXISTS::getResponseException);

        return animalItem;
    }

    public List<AnimalItem> getAnimalItemListByAnimal(Animal animal) {
        return animalItemRepository.findAllByAnimal(animal);
    }

    public List<AnimalItem> getAnimalItemListByPaging(Long animalSeq, Integer pageNumber, Integer pageSize) {

        Animal animal = null;
        if (animalSeq != null) {
            animal = getAnimal(animalSeq);
        }

        return animalItemRepository.findAllByAnimalAndPaging(animal, pageNumber, pageSize);
    }

    public void createAnimalItem(Long animalSeq, Integer turn, String name, MultipartFile colorImage, MultipartFile blackImage, Integer acquisitionRequiredPoint) {

        Animal animal = getAnimal(animalSeq);
        if (isLastTurnOfAnimalItem(animal)) {
            throw ResponseError.BadRequest.INVALID_ANIMAL_SEQ.getResponseException();
        }

        String colorImgUrl = null;
        try {
            colorImgUrl = s3ImageService.uploadFile(colorImage, "animalItem");
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        String blackImgUrl = null;
        try {
            blackImgUrl = s3ImageService.uploadFile(blackImage, "animalItem");
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        AnimalItem animalItem = AnimalItem.builder()
                .animal(animal)
                .turn(turn)
                .name(name)
                .colorImgUrl(colorImgUrl)
                .blackImgUrl(blackImgUrl)
                .acquisitionRequiredPoint(acquisitionRequiredPoint)
                .build();

        try {
            animalItemRepository.save(animalItem);
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_ANIMAL_NAME.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

    }

    public void updateAnimalItem(Long itemSeq, Long animalSeq, Integer turn, String name, MultipartFile colorImage, MultipartFile blackImage, Integer acquisitionRequiredPoint) {

        AnimalItem animalItem = getAnimalItem(itemSeq);

        if (animalItem.getAnimal().getSeq() != animalSeq || animalItem.getTurn() != turn) {
            throw ResponseError.BadRequest.INVALID_SEQ.getResponseException();
        }

        if (name != null) {
            animalItem.setName(name);
        }

        String colorImgUrl = null;
        if (colorImage != null) {
            try {
                colorImgUrl = s3ImageService.uploadFile(colorImage, "animalItem");
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            animalItem.setColorImgUrl(colorImgUrl);
        }

        String blackImgUrl = null;
        if (blackImage != null) {
            try {
                blackImgUrl = s3ImageService.uploadFile(blackImage, "animalItem");
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            animalItem.setBlackImgUrl(blackImgUrl);
        }

        if (acquisitionRequiredPoint != null) {
            animalItem.setAcquisitionRequiredPoint(acquisitionRequiredPoint);
        }

        animalItemRepository.save(animalItem);
    }

    public void deleteAnimalItem(Long seq) {

        AnimalItem animalItem = getAnimalItem(seq);

        adminIslandService.checkLastIsland(animalItem.getAnimal().getIsland());

        animalItemRepository.delete(animalItem);
    }

    public boolean isLastTurnOfAnimalItem(Animal animal) {
        return animalItemRepository.countByAnimal(animal) == Constant.lastTurnOfAnimalItem;
    }
}
