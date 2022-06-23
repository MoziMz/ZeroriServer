package com.mozi.moziserver.service;


import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.repository.IslandImgRepository;
import com.mozi.moziserver.repository.IslandRepository;
import com.mozi.moziserver.repository.UserIslandRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IslandService {
    private final UserRepository userRepository;
    private final IslandRepository islandRepository;
    private final UserIslandRepository userIslandRepository;
    private final IslandImgRepository islandImgRepository;
    private final S3ImageService s3ImageService;
    private final PlatformTransactionManager transactionManager;

    private Island getIsland(Integer type) {
        Island island = islandRepository.findById(type)
                .orElseThrow(ResponseError.NotFound.ISLAND_NOT_EXISTS::getResponseException);

        return island;
    }

    private IslandImg getIslandImg(Integer type,Integer level) {
        IslandImg islandImg = islandImgRepository.findByTypeAndLevel(type,level);

        if(islandImg == null){
            throw ResponseError.NotFound.ISLAND_IMG_NOT_EXISTS.getResponseException();
        }

        return islandImg;
    }

    public List<UserIsland> getUserIslandList(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return userIslandRepository.findAllByUser(user);
    }

    public List<Island> getIslandList() {
        return islandRepository.findAllByOrderByTypeAsc();
    }

    public void createIsland(
            String name,
            Integer type,
            String description,
            Integer maxPoint,
            Integer maxRewardLevel,
            List<MultipartFile> islandImgUrlList
    ){
        final Optional<Island> optionalIsland=islandRepository.findById(type);

        if(optionalIsland.isPresent()){
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException("already created island");
        }

        for(int i =0;i<6;i++){
            String url=islandUploadFile(islandImgUrlList.get(i),i+1);

            final IslandImg islandImg=IslandImg.builder()
                    .type(type)
                    .level(i+1)
                    .imgUrl(url)
                    .build();

            withTransaction(()->{
                try {
                    islandImgRepository.save(islandImg);
                } catch (Exception e) {
                    throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
                }
            });
        }

        final Island island = Island.builder()
                .type(type)
                .name(name)
                .description(description)
                .maxPoint(maxPoint)
                .maxRewardLevel(maxRewardLevel)
                .build();

        withTransaction(()->{
            try {
                islandRepository.save(island);
            } catch (Exception e) {
                throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
            }
        });

    }

    @Transactional
    public void updateIsland(
            String name,
            Integer type,
            String description,
            Integer maxPoint,
            Integer maxRewardLevel
    ) {

        final Island island=getIsland(type);

        if (name != null && name.length() != 0 ) {
            island.setName(name);
        }

        if (description != null && description.length() != 0) {
            island.setDescription(description);
        }

        if(maxPoint != 0 ){
            island.setMaxPoint(maxPoint);
        }

        if(maxRewardLevel !=0 ){
            island.setMaxRewardLevel(maxRewardLevel);
        }

        try {
            islandRepository.save(island);
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    public void updateIslandImg(
            Integer type,
            Integer level,
            MultipartFile islandImgUrl
    ) {

        final IslandImg islandImg=getIslandImg(type,level);

        String imgUrl = null;
        if (islandImgUrl != null) {
            try {
                imgUrl = islandUploadFile(islandImgUrl,level);
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            islandImg.setImgUrl(imgUrl);
        }

        withTransaction(()->{
            try {
                islandImgRepository.save(islandImg);
            } catch (Exception e) {
                throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
            }
        });
    }

    public String islandUploadFile(MultipartFile multipartFile,Integer level){
        try {
            return s3ImageService.uploadFile(multipartFile, "islandImgUrlLevel"+level.toString());
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private void withTransaction(Runnable runnable) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();

        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            runnable.run();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
    }
}
