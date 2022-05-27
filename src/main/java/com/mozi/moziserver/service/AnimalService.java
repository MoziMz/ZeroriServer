package com.mozi.moziserver.service;

import com.mozi.moziserver.common.JpaUtil;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalService {
    private final S3ImageService s3ImageService;
    private final AnimalRepository animalRepository;

    private Animal getAnimal(Long seq) {
        Animal animal = animalRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.ANIMAL_NOT_EXISTS::getResponseException);

        return animal;
    }

    public List<Animal> getAnimalList() {
        return animalRepository.findAll();
    }

    public void createAnimal(String name, String explanation, MultipartFile image) {

        String imgUrl = null;
        try {
            imgUrl = s3ImageService.uploadFile(image, "confirm");
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        Animal animal = Animal.builder()
                .name(name)
                .explanation(explanation)
                .imgUrl(imgUrl)
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

    public void updateAnimal(Long animalSeq, String name, String explanation, MultipartFile image) {

        final Animal animal = getAnimal(animalSeq);

        String imgUrl = null;
        if (image != null) {
            try {
                imgUrl = s3ImageService.uploadFile(image, "confirm");
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            animal.setImgUrl(imgUrl);
        }

        if (name != null && name.length() != 0 && animal.getName() != name) {
            animal.setName(name);
        }

        if (explanation != null && explanation.length() != 0) {
            animal.setExplanation(explanation);
        }

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
}
