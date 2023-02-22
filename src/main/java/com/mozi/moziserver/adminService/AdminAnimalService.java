package com.mozi.moziserver.adminService;

import com.mozi.moziserver.common.JpaUtil;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.repository.AnimalRepository;
import com.mozi.moziserver.service.S3ImageService;
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
public class AdminAnimalService {
//
//    private final AnimalRepository animalRepository;
//    private final PreparationItemRepository preparationItemRepository;
//    private final S3ImageService s3ImageService;
//    private final PlatformTransactionManager transactionManager;
//
//    public Animal getAnimal(Long seq) {
//        Animal animal = animalRepository.findById(seq)
//                .orElseThrow(ResponseError.NotFound.ANIMAL_NOT_EXISTS::getResponseException);
//
//        return animal;
//    }
//
//    private PreparationItem getPreparationItem(Long animalSeq,Integer turn){
//        PreparationItem preparationItem=preparationItemRepository.findByAnimalSeqAndTurn(animalSeq,turn);
//
//        return preparationItem;
//    }
//
//    public List<Animal> getAnimalList() {
//        return animalRepository.findAll();
//    }
//
//    public void createAnimal(String name, String postboxAnimalContent, MultipartFile image, MultipartFile fullBodyImage, Integer islandType, Integer islandLevel) {
//
//        String imgUrl = null;
//        try {
//            imgUrl = s3ImageService.uploadFile(image, "animal");
//        } catch (Exception e) {
//            throw new RuntimeException(e.getCause());
//        }
//
//        String fullBodyImgUrl = null;
//        try {
//            fullBodyImgUrl = s3ImageService.uploadFile(fullBodyImage, "animal");
//        } catch (Exception e) {
//            throw new RuntimeException(e.getCause());
//        }
//
//        Animal animal = Animal.builder()
//                .name(name)
//                .postboxAnimalContent(postboxAnimalContent)
//                .imgUrl(imgUrl)
//                .fullBodyImgUrl(fullBodyImgUrl)
//                .islandType(islandType)
//                .islandLevel(islandLevel)
//                .build();
//
//        try {
//            animalRepository.save(animal);
//        } catch (DataIntegrityViolationException e) {
//            if (JpaUtil.isDuplicateKeyException(e)) {
//                throw ResponseError.BadRequest.ALREADY_EXISTS_ANIMAL_NAME.getResponseException();
//            }
//            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
//        } catch (Exception e) {
//            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
//        }
//    }
//
//    public void updateAnimal(Long animalSeq, String name, String postboxAnimalContent, MultipartFile image, MultipartFile fullBodyImage) {
//
//        final Animal animal = getAnimal(animalSeq);
//
//        String imgUrl = null;
//        if (image != null) {
//            try {
//                imgUrl = s3ImageService.uploadFile(image, "animal");
//            } catch (Exception e) {
//                throw new RuntimeException(e.getCause());
//            }
//            animal.setImgUrl(imgUrl);
//        }
//
//        String fullBodyImgUrl = null;
//        if (fullBodyImage != null) {
//            try {
//                fullBodyImgUrl = s3ImageService.uploadFile(fullBodyImage, "animal");
//            } catch (Exception e) {
//                throw new RuntimeException(e.getCause());
//            }
//            animal.setFullBodyImgUrl(fullBodyImgUrl);
//        }
//
//        if (name != null && name.length() != 0 && animal.getName() != name) {
//            animal.setName(name);
//        }
//
//        if (postboxAnimalContent != null && postboxAnimalContent.length() != 0) {
//            animal.setPostboxAnimalContent(postboxAnimalContent);
//        }
//
//        try {
//            animalRepository.save(animal);
//        } catch (DataIntegrityViolationException e) {
//            if (JpaUtil.isDuplicateKeyException(e)) {
//                throw ResponseError.BadRequest.ALREADY_EXISTS_ANIMAL_NAME.getResponseException();
//            }
//            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
//        } catch (Exception e) {
//            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
//        }
//    }
//
//    public void createPreparationItem(Long animalSeq, Integer turn, String name, MultipartFile colorImage, MultipartFile blackImage) {
//
//        final PreparationItem preparationItemCheck=getPreparationItem(animalSeq,turn);
//
//        if(preparationItemCheck != null ){
//            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();
//        }
//
//        final Animal animal = getAnimal(animalSeq);
//
//        String colorImgUrl = null;
//        try {
//            colorImgUrl = s3ImageService.uploadFile(colorImage, "preparationItem");
//        } catch (Exception e) {
//            throw new RuntimeException(e.getCause());
//        }
//
//        String blackImgUrl = null;
//        try {
//            blackImgUrl = s3ImageService.uploadFile(blackImage, "preparationItem");
//        } catch (Exception e) {
//            throw new RuntimeException(e.getCause());
//        }
//
//        final PreparationItem preparationItem = PreparationItem.builder()
//                .animal(animal)
//                .turn(turn)
//                .name(name)
//                .colorImgUrl(colorImgUrl)
//                .blackImgUrl(blackImgUrl)
//                .build();
//
//        // TODO turn에 대한 고민..
//        preparationItemRepository.save(preparationItem);
//    }
//
//    public void updatePreparationItem(Long animalSeq, Integer turn, String name, MultipartFile colorImage, MultipartFile blackImage) {
//
//        final Animal animal = getAnimal(animalSeq);
//
//        final PreparationItem preparationItem= getPreparationItem(animalSeq,turn);
//
//        if(preparationItem==null){
//            throw ResponseError.NotFound.PREPARATIONITEM_NOT_EXISTS.getResponseException();
//        }
//
//        if(name != null){
//            preparationItem.setName(name);
//        }
//
//        if(colorImage !=null ){
//            String colorImgUrl = null;
//            try {
//                colorImgUrl = s3ImageService.uploadFile(colorImage, "preparationItem");
//                preparationItem.setColorImgUrl(colorImgUrl);
//            } catch (Exception e) {
//                throw new RuntimeException(e.getCause());
//            }
//        }
//
//        if(blackImage != null ){
//            String blackImgUrl = null;
//            try {
//                blackImgUrl = s3ImageService.uploadFile(blackImage, "preparationItem");
//                preparationItem.setBlackImgUrl(blackImgUrl);
//            } catch (Exception e) {
//                throw new RuntimeException(e.getCause());
//            }
//
//        }
//
//        withTransaction(()->{
//            try {
//                preparationItemRepository.save(preparationItem);
//            } catch (Exception e) {
//                throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
//            }
//        });
//    }
//
//    private void withTransaction(Runnable runnable) {
//        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
//
//        TransactionStatus status = transactionManager.getTransaction(definition);
//        try {
//            runnable.run();
//            transactionManager.commit(status);
//        } catch (Exception e) {
//            transactionManager.rollback(status);
//        }
//    }
}
