package com.mozi.moziserver.adminService;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.DetailIsland;
import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.repository.DetailIslandRepository;
import com.mozi.moziserver.repository.IslandRepository;
import com.mozi.moziserver.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminIslandService {

    private final S3ImageService s3ImageService;

    private final IslandRepository islandRepository;
    private final DetailIslandRepository detailIslandRepository;

//    private final PlatformTransactionManager transactionManager;

    public Island getIsland(Long seq) {

        Island island = islandRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.ISLAND_NOT_EXISTS::getResponseException);

        return island;
    }

    public void updateImgOfDetailIsland(Long seq, MultipartFile img, MultipartFile islandThumbnailImg) {

        DetailIsland detailIsland = detailIslandRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.NOT_EXISTS::getResponseException);

        String imgUrl = null;
        if (img != null) {
            try {
                imgUrl = s3ImageService.uploadFile(img, "detailIsland");
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            detailIsland.setImgUrl(imgUrl);
        }

        String islandThumbnailImgUrl = null;
        if (islandThumbnailImg != null) {
            try {
                islandThumbnailImgUrl = s3ImageService.uploadFile(islandThumbnailImg, "detailIsland");
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            detailIsland.setThumbnailImgUrl(islandThumbnailImgUrl);
        }

        detailIslandRepository.save(detailIsland);

    }

    public void checkLastIsland(Island island){
        if(island.getSeq() == Constant.lastIslandSeq){
            throw ResponseError.BadRequest.INVALID_SEQ.getResponseException("Creation/Deletion is not possible because the island seq is 1.");
        }
    }
//
//    public List<Island> getIslandList() {
//        return islandRepository.findAll();
//    }
//
//    private IslandImg getIslandImg(Integer type, Integer level) {
//        IslandImg islandImg = islandImgRepository.findByTypeAndLevel(type, level);
//
//        if (islandImg == null) {
//            throw ResponseError.NotFound.ISLAND_IMG_NOT_EXISTS.getResponseException();
//        }
//
//        return islandImg;
//    }
//
//    public List<IslandImg> getIslandImgListByType(Integer islandType) {
//        return islandImgRepository.findAllByType(islandType);
//    }
//
//    public void createIsland(
//            Integer type,
//            String name,
//            String description,
//            Integer maxPoint,
//            Integer maxRewardLevel,
//            List<MultipartFile> islandImgUrlList,
//            List<MultipartFile> islandThumbnailImgUrlList
//    ) {
//        final Optional<Island> optionalIsland = islandRepository.findById(type);
//
//        if (optionalIsland.isPresent()) {
//            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException("already created island");
//        }
//
//        for (int i = 0; i < Constant.islandMaxLevel; i++) {
//            String imgUrl = islandUploadFile(islandImgUrlList.get(i), i + 1);
//            String thumbnailImgUrl = islandUploadFile(islandThumbnailImgUrlList.get(i), i + 1);
//
//            final IslandImg islandImg = IslandImg.builder()
//                    .type(type)
//                    .level(i + 1)
//                    .imgUrl(imgUrl)
//                    .thumbnailImgUrl(thumbnailImgUrl)
//                    .build();
//
//            withTransaction(() -> {
//                try {
//                    islandImgRepository.save(islandImg);
//                } catch (Exception e) {
//                    throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
//                }
//            });
//        }
//
//        final Island island = Island.builder()
//                .type(type)
//                .name(name)
//                .description(description)
//                .maxPoint(maxPoint)
//                .maxRewardLevel(maxRewardLevel)
//                .build();
//
//        withTransaction(() -> {
//            try {
//                islandRepository.save(island);
//            } catch (Exception e) {
//                throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
//            }
//        });
//    }
//
//    public void updateIsland(
//            Integer type,
//            String name,
//            String description,
//            Integer maxPoint,
//            Integer maxRewardLevel
//    ) {
//        final Island island = getIsland(type);
//
//        if (name != null && name.length() != 0) {
//            island.setName(name);
//        }
//
//        if (description != null && description.length() != 0) {
//            island.setDescription(description);
//        }
//
//        if (maxPoint != 0) {
//            island.setMaxPoint(maxPoint);
//        }
//
//        if (maxRewardLevel != 0) {
//            island.setMaxRewardLevel(maxRewardLevel);
//        }
//
//        try {
//            islandRepository.save(island);
//        } catch (Exception e) {
//            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
//        }
//    }
//
//    public void updateIslandImg(
//            Integer type,
//            Integer level,
//            MultipartFile islandImgFile,
//            MultipartFile islandThumbnailImgFile
//    ) {
//        final IslandImg islandImg = getIslandImg(type, level);
//
//        String islandImgUrl = null;
//        if (islandImgFile != null) {
//            try {
//                islandImgUrl = islandUploadFile(islandImgFile, level);
//            } catch (Exception e) {
//                throw new RuntimeException(e.getCause());
//            }
//            islandImg.setImgUrl(islandImgUrl);
//        }
//
//        String islandThumbnailImgUrl = null;
//        if (islandThumbnailImgFile != null) {
//            try {
//                islandThumbnailImgUrl = islandUploadFile(islandThumbnailImgFile, level);
//            } catch (Exception e) {
//                throw new RuntimeException(e.getCause());
//            }
//            islandImg.setImgUrl(islandThumbnailImgUrl);
//        }
//
//        if (islandImgUrl != null) {
//            islandImg.setImgUrl(islandImgUrl);
//        }
//        if (islandThumbnailImgUrl != null) {
//            islandImg.setThumbnailImgUrl(islandThumbnailImgUrl);
//        }
//
//        withTransaction(() -> {
//            try {
//                islandImgRepository.save(islandImg);
//            } catch (Exception e) {
//                throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
//            }
//        });
//    }
//
//    public String islandUploadFile(MultipartFile multipartFile, Integer level) {
//        try {
//            return s3ImageService.uploadFile(multipartFile, "islandImgUrlLevel" + level.toString());
//        } catch (Exception e) {
//            throw new RuntimeException(e.getCause());
//        }
//    }
//
//    public void deleteIsland(Integer type) {
//        final Island island = getIsland(type);
//
//        islandRepository.delete(island);
//        islandImgRepository.deleteByType(island.getType());
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
